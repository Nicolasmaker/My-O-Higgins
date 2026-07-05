import { useEffect, useMemo, useState } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'react-toastify'
import { useAuth } from '../../hooks/useAuth'
import {
  actualizarAnotacion,
  crearAnotacion,
  eliminarAnotacion,
  getAllAnotaciones,
  getAnotacionesByRut,
} from '../../services/anotacionesService'
import { getHojaDeVidaPorRut } from '../../services/hojaDeVidaService'
import { limpiarRut, rutValido } from '../../validators/fieldValidators'
import AnotacionCard from '../../components/Anotaciones/AnotacionCard'
import AnotacionesToolbar from '../../components/Anotaciones/AnotacionesToolbar'
import AnotacionForm from '../../components/Anotaciones/AnotacionForm'
import './anotaciones.css'

// Segun los comentarios de permiso en MS-Anotaciones (AnotacionController.java):
// crear -> Docentes e Inspectores | editar/eliminar -> solo Directivos
// El backend aun no valida esto (ver SecurityConfig sin reglas por rol), asi que
// aqui solo se oculta la UI segun el rol; no reemplaza una autorizacion real.
const ROLES_CREAN = ['ROLE_DOCENTE', 'ROLE_INSPECTOR']
const ROLES_GESTIONAN = ['ROLE_DIRECTIVO']
const ROL_ESTUDIANTE = 'ROLE_ESTUDIANTE'

const initialForm = {
  anotTip: 'Positiva',
  anotDes: '',
  rutEstudiante: '',
}

function formatDate(value) {
  if (!value) return 'Sin fecha'
  return new Date(value).toLocaleDateString('es-CL')
}

export default function Anotaciones() {
  const { usuario, hasRole } = useAuth()
  const [anotaciones, setAnotaciones] = useState([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')
  const [saving, setSaving] = useState(false)
  const [filterRut, setFilterRut] = useState('')
  const [activeFilterRut, setActiveFilterRut] = useState('')
  const [filterTipo, setFilterTipo] = useState('todas')
  const [filterCurso, setFilterCurso] = useState('')
  const [editingId, setEditingId] = useState(null)
  const [hojaVidaStatus, setHojaVidaStatus] = useState('idle')
  const [idHojaVidaResuelto, setIdHojaVidaResuelto] = useState(null)

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm({ defaultValues: initialForm })

  const userRut = useMemo(() => usuario?.usuRut ?? usuario?.rut ?? usuario?.rutUsuario ?? '', [usuario])
  const canCreate = hasRole(ROLES_CREAN)
  const canManage = hasRole(ROLES_GESTIONAN)
  const isEstudiante = hasRole(ROL_ESTUDIANTE)
  const rutEstudianteTyped = watch('rutEstudiante')

  // Normaliza texto para comparar sin importar tildes, mayúsculas ni símbolos (ej. "8°B" vs "8b").
  const normalizar = (valor) =>
    String(valor || '')
      .normalize('NFD')
      .replace(/[^a-z0-9]/gi, '')
      .toLowerCase()

  const anotacionesFiltradas = useMemo(() => {
    const cursoBuscado = normalizar(filterCurso)
    const textoBuscado = normalizar(filterRut)

    return anotaciones.filter((item) => {
      if (filterTipo !== 'todas' && String(item.anotTip || '').toLowerCase() !== filterTipo) {
        return false
      }
      if (cursoBuscado && !normalizar(item.curso).includes(cursoBuscado)) {
        return false
      }
      if (textoBuscado) {
        const nombreCompleto = normalizar(`${item.estudianteNombre || ''} ${item.estudianteApellido || ''}`)
        const rutEstudiante = normalizar(item.estudianteUsuRut)
        if (!nombreCompleto.includes(textoBuscado) && !rutEstudiante.includes(textoBuscado)) {
          return false
        }
      }
      return true
    })
  }, [anotaciones, filterTipo, filterCurso, filterRut])

  const dashboardStats = useMemo(() => {
    const positivas = anotacionesFiltradas.filter((item) => String(item.anotTip || '').toLowerCase() === 'positiva').length
    const negativas = anotacionesFiltradas.filter((item) => String(item.anotTip || '').toLowerCase() === 'negativa').length

    return {
      total: anotacionesFiltradas.length,
      positivas,
      negativas,
    }
  }, [anotacionesFiltradas])

  // Resuelve automáticamente el idHojaVida a partir del RUT tipeado en el formulario
  // de creación (RF11: la anotación se relaciona con la hoja de vida del estudiante).
  useEffect(() => {
    if (editingId) return
    const valor = (rutEstudianteTyped || '').trim()
    if (!valor || !rutValido(valor)) {
      setHojaVidaStatus('idle')
      setIdHojaVidaResuelto(null)
      return
    }

    let cancelado = false
    setHojaVidaStatus('buscando')
    const timeout = setTimeout(() => {
      getHojaDeVidaPorRut(limpiarRut(valor))
        .then((res) => {
          if (cancelado) return
          setIdHojaVidaResuelto(res.data?.idHojaVida ?? null)
          setHojaVidaStatus('encontrada')
        })
        .catch(() => {
          if (cancelado) return
          setIdHojaVidaResuelto(null)
          setHojaVidaStatus('no-encontrada')
        })
    }, 500)

    return () => {
      cancelado = true
      clearTimeout(timeout)
    }
  }, [rutEstudianteTyped, editingId])

  const loadAll = async () => {
    setLoading(true)
    setLoadError('')
    setActiveFilterRut('')

    try {
      const response = await getAllAnotaciones()
      setAnotaciones(Array.isArray(response.data) ? response.data : [])
    } catch (error) {
      console.error(error)
      const message = error.response?.data?.message || 'No se pudieron cargar las anotaciones'
      setLoadError(message)
      toast.error(message)
      setAnotaciones([])
    } finally {
      setLoading(false)
    }
  }

  const loadByRut = async (rut) => {
    setLoading(true)
    setLoadError('')

    try {
      const response = await getAnotacionesByRut(limpiarRut(rut))
      setAnotaciones(Array.isArray(response.data) ? response.data : [])
      setActiveFilterRut(rut)
    } catch (error) {
      console.error(error)
      const message = error.response?.data?.message || 'No se pudieron cargar las anotaciones del estudiante'
      setLoadError(message)
      toast.error(message)
      setAnotaciones([])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    // Un estudiante solo puede ver sus propias anotaciones: se carga directo con
    // su RUT y no tiene acceso al filtro de búsqueda por RUT de otros estudiantes.
    if (isEstudiante) {
      if (userRut) loadByRut(userRut)
      return
    }
    loadAll()
  }, [isEstudiante, userRut])

  // El filtro por tipo/curso/nombre ya se aplica en vivo sobre lo cargado (anotacionesFiltradas).
  // Este submit solo se usa cuando lo tipeado es un RUT valido: en ese caso ademas trae del
  // backend el historial completo de ese estudiante (no solo lo que ya estaba cargado).
  const handleSearch = async (event) => {
    event.preventDefault()
    const valor = filterRut.trim()
    if (!valor) {
      await loadAll()
      return
    }
    if (!rutValido(valor)) {
      return
    }
    await loadByRut(valor)
  }

  const handleResetFiltros = async () => {
    setFilterRut('')
    setFilterTipo('todas')
    setFilterCurso('')
    await loadAll()
  }

  const handleRefresh = async () => {
    if (activeFilterRut) {
      await loadByRut(activeFilterRut)
    } else {
      await loadAll()
    }
  }

  const resetForm = () => {
    setEditingId(null)
    reset(initialForm)
    setHojaVidaStatus('idle')
    setIdHojaVidaResuelto(null)
  }

  const onSubmit = async (data) => {
    setSaving(true)

    try {
      if (editingId) {
        await actualizarAnotacion(editingId, {
          anotTip: data.anotTip,
          anotDes: data.anotDes,
        })
        toast.success('Anotación actualizada')
      } else {
        if (!idHojaVidaResuelto) {
          toast.error('Ingresa el RUT de un estudiante con hoja de vida registrada')
          setSaving(false)
          return
        }
        await crearAnotacion({
          anotTip: data.anotTip,
          anotDes: data.anotDes,
          funcionarioUsuRut: Number(userRut),
          idHojaVida: idHojaVidaResuelto,
        })
        toast.success('Anotación creada')
      }

      resetForm()
      if (activeFilterRut) {
        await loadByRut(activeFilterRut)
      } else {
        await loadAll()
      }
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo guardar la anotación')
    } finally {
      setSaving(false)
    }
  }

  const handleEdit = (anotacion) => {
    setEditingId(anotacion.idAnot)
    reset({
      anotTip: anotacion.anotTip || 'Positiva',
      anotDes: anotacion.anotDes || '',
      rutEstudiante: '',
    })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const handleDelete = async (id) => {
    const shouldDelete = window.confirm('¿Eliminar esta anotación?')
    if (!shouldDelete) return

    try {
      await eliminarAnotacion(id)
      toast.success('Anotación eliminada')
      if (activeFilterRut) {
        await loadByRut(activeFilterRut)
      } else {
        await loadAll()
      }
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo eliminar la anotación')
    }
  }

  const mostrarFormulario = canCreate || editingId

  return (
    <div className="anotaciones-page">
      <main className="anotaciones-shell">
        <section className="hero-card">
          <div className="hero-copy-block">
            <h1>Gestión institucional de anotaciones con trazabilidad completa</h1>
            <p className="hero-copy">
              {isEstudiante
                ? 'Revisa aquí el historial completo de tus anotaciones.'
                : 'Busca a un estudiante por su RUT para revisar su historial, o registra una nueva anotación indicando el RUT: la hoja de vida se detecta automáticamente.'}
            </p>
          </div>
        </section>

        {isEstudiante ? (
          <p className="anotaciones-estudiante-aviso">Estás viendo únicamente tus propias anotaciones.</p>
        ) : (
          <AnotacionesToolbar
            filterRut={filterRut}
            setFilterRut={setFilterRut}
            onSearch={handleSearch}
            onReset={handleResetFiltros}
            onRefresh={handleRefresh}
            filterTipo={filterTipo}
            setFilterTipo={setFilterTipo}
            filterCurso={filterCurso}
            setFilterCurso={setFilterCurso}
          />
        )}

        <section className="metrics-grid" aria-label="Resumen de anotaciones">
          <article className="metric-card">
            <span>Total</span>
            <strong>{dashboardStats.total}</strong>
          </article>
          <article className="metric-card">
            <span>Positivas</span>
            <strong>{dashboardStats.positivas}</strong>
          </article>
          <article className="metric-card">
            <span>Negativas</span>
            <strong>{dashboardStats.negativas}</strong>
          </article>
        </section>

        <section className={`content-grid ${mostrarFormulario ? '' : 'content-grid--single'}`}>
          {mostrarFormulario && (
            <AnotacionForm
              register={register}
              errors={errors}
              isSaving={saving}
              editingId={editingId}
              onSubmit={handleSubmit(onSubmit)}
              onCancel={resetForm}
              hojaVidaStatus={hojaVidaStatus}
              idHojaVidaResuelto={idHojaVidaResuelto}
            />
          )}

          <section className="list-card">
            <div className="list-header">
              <div>
                <p className="eyebrow">Listado institucional</p>
                <h2>Anotaciones registradas</h2>
              </div>
              <span className="list-header__counter">{anotacionesFiltradas.length} registros</span>
            </div>

            {loading ? (
              <div className="empty-state">Cargando anotaciones...</div>
            ) : loadError ? (
              <div className="empty-state empty-state--error">{loadError}</div>
            ) : anotaciones.length === 0 ? (
              <div className="empty-state">
                {activeFilterRut
                  ? 'Este estudiante no tiene anotaciones registradas.'
                  : 'No hay anotaciones para mostrar.'}
              </div>
            ) : anotacionesFiltradas.length === 0 ? (
              <div className="empty-state">Ninguna anotación coincide con los filtros aplicados.</div>
            ) : (
              <div className="cards-list">
                {anotacionesFiltradas.map((anotacion) => (
                  <AnotacionCard
                    key={anotacion.idAnot}
                    anotacion={anotacion}
                    onEdit={handleEdit}
                    onDelete={handleDelete}
                    formatDate={formatDate}
                    canManage={canManage}
                  />
                ))}
              </div>
            )}
          </section>
        </section>
      </main>
    </div>
  )
}
