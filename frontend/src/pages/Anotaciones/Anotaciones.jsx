import { useEffect, useMemo, useState } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'react-toastify'
import { useAuth } from '../../hooks/useAuth'
import {
  crearAnotacion,
  actualizarAnotacion,
  eliminarAnotacion,
  getAllAnotaciones,
  getAnotacionesByHojaVida,
} from '../../services/anotacionesService'
import Button from '../../components/UI/Button/Button'
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

  const loadAnotaciones = async (hojaVidaId = '') => {
    setLoading(true)
    try {
      const response = hojaVidaId
        ? await getAnotacionesByHojaVida(hojaVidaId)
        : await getAllAnotaciones()
      setAnotaciones(Array.isArray(response.data) ? response.data : [])
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudieron cargar las anotaciones')
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
    <main className="anotaciones-page">
      <section className="hero-card">
        <div>
          <p className="eyebrow">MS-Anotaciones</p>
          <h1>Panel simple para gestionar anotaciones</h1>
          <p className="hero-copy">
            Vista conectada al backend para crear, editar, listar y borrar anotaciones de estudiantes.
          </p>
        </div>

        <div className="session-card">
          <span className="session-label">Sesión</span>
          <strong>{usuario?.usuPNombre ? `${usuario.usuPNombre} ${usuario.usuApePat || ''}`.trim() : 'Sesión no identificada'}</strong>
          <span>RUT: {userRut || 'sin dato'}</span>
          <span>Rol: {usuario?.rol?.rolNombre || usuario?.rol || 'sin dato'}</span>
          <Button type="button" variant="ghost" onClick={logout}>
            Cerrar sesión
          </Button>
        </div>
      </section>

      <AnotacionesToolbar
        total={anotaciones.length}
        modeLabel={editingId ? 'Edición' : 'Creación'}
        canSeeLabel={hasRole(['ADMIN', 'DIRECTIVO', 'DOCENTE', 'INSPECTOR']) ? 'Activo' : 'General'}
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
          <div className="form-header">
            <div>
              <p className="eyebrow">Listado</p>
              <h2>Anotaciones registradas</h2>
            </div>
          </div>

          {loading ? (
            <div className="empty-state">Cargando anotaciones...</div>
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
  )
}
