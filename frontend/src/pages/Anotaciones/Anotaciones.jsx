import { useEffect, useMemo, useState } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'react-toastify'
import { useAuth } from '../../hooks/useAuth'
import {
  actualizarAnotacion,
  crearAnotacion,
  eliminarAnotacion,
  getAllAnotaciones,
  getAnotacionesByHojaVida,
} from '../../services/anotacionesService'
import Button from '../../components/UI/Button'
import AnotacionCard from '../../components/Anotaciones/AnotacionCard'
import AnotacionesToolbar from '../../components/Anotaciones/AnotacionesToolbar'
import AnotacionForm from '../../components/Anotaciones/AnotacionForm'
import './anotaciones.css'

const initialForm = {
  anotTip: 'Positiva',
  anotDes: '',
  funcionarioUsuRut: '',
  idHojaVida: '',
}

function formatDate(value) {
  if (!value) return 'Sin fecha'
  return new Date(value).toLocaleDateString('es-CL')
}

export default function Anotaciones() {
  const { usuario, hasRole, logout } = useAuth()
  const [anotaciones, setAnotaciones] = useState([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')
  const [saving, setSaving] = useState(false)
  const [filterHojaVida, setFilterHojaVida] = useState('')
  const [editingId, setEditingId] = useState(null)

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    formState: { errors },
  } = useForm({ defaultValues: initialForm })

  const userRut = useMemo(() => usuario?.usuRut ?? usuario?.rut ?? usuario?.rutUsuario ?? '', [usuario])
  const roleLabel = useMemo(
    () => usuario?.rol?.rolNombre || usuario?.rolNombre || usuario?.rol || 'Sin rol asignado',
    [usuario]
  )

  const dashboardStats = useMemo(() => {
    const positivas = anotaciones.filter((item) => String(item.anotTip || '').toLowerCase() === 'positiva').length
    const negativas = anotaciones.filter((item) => String(item.anotTip || '').toLowerCase() === 'negativa').length

    return {
      total: anotaciones.length,
      positivas,
      negativas,
      filtro: filterHojaVida.trim() ? `Hoja ${filterHojaVida.trim()}` : 'Sin filtro',
    }
  }, [anotaciones, filterHojaVida])

  const loadAnotaciones = async (hojaVidaId = '') => {
    setLoading(true)
    setLoadError('')

    try {
      const response = hojaVidaId
        ? await getAnotacionesByHojaVida(hojaVidaId)
        : await getAllAnotaciones()

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

  useEffect(() => {
    loadAnotaciones()
  }, [])

  useEffect(() => {
    if (userRut) {
      setValue('funcionarioUsuRut', String(userRut), { shouldValidate: true })
    }
  }, [userRut, setValue])

  const onSubmit = async (data) => {
    setSaving(true)

    const payload = {
      anotTip: data.anotTip,
      anotDes: data.anotDes,
      funcionarioUsuRut: Number(data.funcionarioUsuRut),
      idHojaVida: Number(data.idHojaVida),
    }

    try {
      if (editingId) {
        await actualizarAnotacion(editingId, payload)
        toast.success('Anotación actualizada')
      } else {
        await crearAnotacion(payload)
        toast.success('Anotación creada')
      }

      reset({
        ...initialForm,
        funcionarioUsuRut: String(userRut || ''),
      })
      setEditingId(null)
      await loadAnotaciones(filterHojaVida)
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
      funcionarioUsuRut: String(anotacion.funcionarioUsuRut ?? ''),
      idHojaVida: String(anotacion.idHojaVida ?? ''),
    })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const handleDelete = async (id) => {
    const shouldDelete = window.confirm('¿Eliminar esta anotación?')
    if (!shouldDelete) return

    try {
      await eliminarAnotacion(id)
      toast.success('Anotación eliminada')
      await loadAnotaciones(filterHojaVida)
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo eliminar la anotación')
    }
  }

  const handleFilter = async (event) => {
    event.preventDefault()
    await loadAnotaciones(filterHojaVida.trim())
  }

  const resetForm = () => {
    setEditingId(null)
    reset({
      ...initialForm,
      funcionarioUsuRut: String(userRut || ''),
    })
  }

  return (
    <div className="anotaciones-page">
      <main className="anotaciones-shell">
        <section className="hero-card">
          <div className="hero-copy-block">

            <h1>Gestión institucional de anotaciones con trazabilidad completa</h1>

            <div className="hero-actions">
              <Button type="button" variant="outline" onClick={() => loadAnotaciones(filterHojaVida.trim())}>
                Refrescar datos
              </Button>
              <Button
                type="button"
                variant="ghost"
                onClick={() => {
                  setFilterHojaVida('')
                  loadAnotaciones()
                }}
              >
                Limpiar filtro
              </Button>
            </div>
          </div>

          <aside className="session-card">
            <span className="session-label">Sesión activa</span>
            <strong>{usuario?.usuPNombre ? `${usuario.usuPNombre} ${usuario.usuApePat || ''}`.trim() : 'Sesión no identificada'}</strong>
            <span>RUT: {userRut || 'sin dato'}</span>
            <span>Rol: {roleLabel}</span>
            <span>Acceso: {hasRole(['ADMIN', 'DIRECTIVO', 'DOCENTE', 'INSPECTOR']) ? 'Habilitado' : 'General'}</span>
            <Button type="button" variant="ghost" onClick={logout}>
              Cerrar sesión
            </Button>
          </aside>
        </section>

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
          <article className="metric-card metric-card--accent">
            <span>Filtro actual</span>
            <strong>{dashboardStats.filtro}</strong>
          </article>
        </section>

        <AnotacionesToolbar
          total={dashboardStats.total}
          modeLabel={editingId ? 'Edición activa' : 'Creación disponible'}
          canSeeLabel={hasRole(['ADMIN', 'DIRECTIVO', 'DOCENTE', 'INSPECTOR']) ? 'Perfil autorizado' : 'Perfil general'}
          filterHojaVida={filterHojaVida}
          setFilterHojaVida={setFilterHojaVida}
          onSearch={handleFilter}
          onReset={() => {
            setFilterHojaVida('')
            loadAnotaciones()
          }}
        />

        <section className="content-grid">
          <AnotacionForm
            register={register}
            errors={errors}
            isSaving={saving}
            editingId={editingId}
            onSubmit={handleSubmit(onSubmit)}
            onCancel={resetForm}
          />

          <section className="list-card">
            <div className="list-header">
              <div>
                <p className="eyebrow">Listado institucional</p>
                <h2>Anotaciones registradas</h2>
              </div>
              <span className="list-header__counter">{anotaciones.length} registros</span>
            </div>

            {loading ? (
              <div className="empty-state">Cargando anotaciones...</div>
            ) : loadError ? (
              <div className="empty-state empty-state--error">{loadError}</div>
            ) : anotaciones.length === 0 ? (
              <div className="empty-state">No hay anotaciones para mostrar.</div>
            ) : (
              <div className="cards-list">
                {anotaciones.map((anotacion) => (
                  <AnotacionCard
                    key={anotacion.idAnot}
                    anotacion={anotacion}
                    onEdit={handleEdit}
                    onDelete={handleDelete}
                    formatDate={formatDate}
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
