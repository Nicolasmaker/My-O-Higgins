import { useEffect, useMemo, useState } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'react-toastify'
import { FiCalendar, FiChevronLeft, FiChevronRight, FiClock, FiList, FiPlus, FiRefreshCw } from 'react-icons/fi'
import { Modal } from 'react-bootstrap' // <-- Agregamos la importación del Modal
import Button from '../../components/UI/Button'
import Input from '../../components/UI/Input'
import { useAuth } from '../../hooks/useAuth'
import { crearEvento, actualizarEvento, eliminarEvento, getEventos, crearMural } from '../../services/calendarioService'
import { getAsignaturas } from '../../services/asignaturaService'
import { getImpartirByDocente } from '../../services/academicoService'
import { getMatriculas } from '../../services/matriculaService'
import styles from '../../styles/Calendario.module.css'

const eventTypes = ['Institucional', 'Académico', 'Actividad']
// Institucional/Actividad no tienen asignatura real; Mural Digital solo recibe estos dos tipos.
const tiposSinAsignatura = ['Institucional', 'Actividad']
const tiposParaMural = ['Institucional', 'Actividad']

const initialForm = {
  tituloEvento: '',
  tipoEvento: 'Académico',
  fechaInicio: '',
  fechaFin: '',
  idAsignatura: '',
  descripcionEvento: '',
}

function pad(number) {
  return String(number).padStart(2, '0')
}

function toInputDate(date) {
  const year = date.getFullYear()
  const month = pad(date.getMonth() + 1)
  const day = pad(date.getDate())
  return `${year}-${month}-${day}`
}

function fromInputDate(value) {
  if (!value) return null
  const [year, month, day] = value.split('-').map(Number)
  return new Date(year, month - 1, day)
}

function addDays(date, amount) {
  const next = new Date(date)
  next.setDate(next.getDate() + amount)
  return next
}

function startOfMonth(date) {
  return new Date(date.getFullYear(), date.getMonth(), 1)
}

function startOfWeek(date) {
  const current = new Date(date)
  const mondayOffset = (current.getDay() + 6) % 7
  current.setDate(current.getDate() - mondayOffset)
  return current
}

function isSameDay(dateA, dateB) {
  return (
    dateA.getFullYear() === dateB.getFullYear() &&
    dateA.getMonth() === dateB.getMonth() &&
    dateA.getDate() === dateB.getDate()
  )
}

function isInsideRange(target, start, end) {
  const targetValue = target.getTime()
  return targetValue >= start.getTime() && targetValue <= end.getTime()
}

function formatLongDate(value) {
  const date = typeof value === 'string' ? fromInputDate(value) : value
  if (!date) return 'Sin fecha'
  return new Intl.DateTimeFormat('es-CL', {
    day: '2-digit',
    month: 'long',
    year: 'numeric',
  }).format(date)
}

function formatMonthLabel(date) {
  return new Intl.DateTimeFormat('es-CL', {
    month: 'long',
    year: 'numeric',
  }).format(date)
}

function formatWeekday(date) {
  return new Intl.DateTimeFormat('es-CL', { weekday: 'short' }).format(date)
}

function eventTone(tipo) {
  const normalized = String(tipo || '').toLowerCase()
  if (normalized.includes('institucional')) return 'institutional'
  if (normalized.includes('reunion')) return 'meeting'
  if (normalized.includes('reunión')) return 'meeting'
  if (normalized.includes('actividad')) return 'activity'
  return 'academic'
}

function toneLabel(tipo) {
  const normalized = String(tipo || '')
  if (normalized.toLowerCase().includes('institucional')) return 'Institucional'
  if (normalized.toLowerCase().includes('reun')) return 'Reunión'
  if (normalized.toLowerCase().includes('actividad')) return 'Actividad'
  return 'Académico'
}

export default function Calendario() {
  const { usuario, hasRole } = useAuth()
  const esDirectivo = hasRole('ROLE_DIRECTIVO')
  const esDocente = hasRole('ROLE_DOCENTE')
  const esInspector = hasRole('ROLE_INSPECTOR')
  const esApoderado = hasRole('ROLE_APODERADO')
  const esEstudiante = hasRole('ROLE_ESTUDIANTE')
  const puedeGestionar = esDirectivo || esDocente

  // Matrículas: para resolver "de qué curso es este usuario"
  const [matriculas, setMatriculas] = useState([])
  useEffect(() => {
    getMatriculas()
      .then((r) => setMatriculas(Array.isArray(r.data) ? r.data : []))
      .catch(() => setMatriculas([]))
  }, [])
  const cursosHijosApoderado = useMemo(
    () =>
      new Set(
        matriculas
          .filter((m) => String(m.apoderadoRut) === String(usuario?.usuRut))
          .map((m) => m.cursoId)
          .filter((id) => id != null)
      ),
    [matriculas, usuario?.usuRut]
  )
  const cursoPropioEstudiante = useMemo(() => {
    const propia = matriculas.find((m) => String(m.alumnoRut) === String(usuario?.usuRut))
    return propia?.cursoId ?? null
  }, [matriculas, usuario?.usuRut])

  // Docente: solo puede crear/editar/eliminar eventos "Académico" de sus propias asignaturas.
  const [impartirDocente, setImpartirDocente] = useState([])
  useEffect(() => {
    if (!esDocente || !usuario?.usuRut) return
    getImpartirByDocente(usuario.usuRut)
      .then((r) => setImpartirDocente(Array.isArray(r.data) ? r.data : []))
      .catch(() => setImpartirDocente([]))
  }, [esDocente, usuario?.usuRut])
  const idsAsignaturaPropias = useMemo(
    () => new Set(impartirDocente.map((i) => i.asignatura?.idAsi).filter(Boolean)),
    [impartirDocente]
  )

  const puedeEditarEvento = (evento) => {
    if (esDirectivo) return true
    if (esDocente) return evento?.tipoEvento === 'Académico' && idsAsignaturaPropias.has(evento?.idAsignatura)
    return false
  }

  // Visibilidad por audiencia
  const eventoVisiblePorAudiencia = (evento) => {
    if (esDirectivo || esInspector) return true
    const sinAudiencia =
      evento.cursoId == null &&
      evento.docenteUsuRut == null &&
      evento.apoderadoUsuRut == null &&
      evento.alumnoRut == null
    if (sinAudiencia) return true
    if (esDocente) return String(evento.docenteUsuRut) === String(usuario?.usuRut)
    if (esApoderado) {
      return (
        (evento.cursoId != null && cursosHijosApoderado.has(evento.cursoId)) ||
        String(evento.apoderadoUsuRut) === String(usuario?.usuRut)
      )
    }
    if (esEstudiante) {
      return (
        (evento.cursoId != null && cursoPropioEstudiante != null && Number(evento.cursoId) === Number(cursoPropioEstudiante)) ||
        String(evento.alumnoRut) === String(usuario?.usuRut)
      )
    }
    return false
  }

  const today = useMemo(() => new Date(), [])
  const [eventos, setEventos] = useState([])
  const [asignaturas, setAsignaturas] = useState([])
  const asignaturasParaForm = useMemo(() => {
    if (!esDocente) return asignaturas
    return asignaturas.filter((a) => idsAsignaturaPropias.has(a.idAsi))
  }, [asignaturas, esDocente, idsAsignaturaPropias])
  const [loading, setLoading] = useState(true)
  
  // Estados para el Modal de Confirmación
  const [showConfirm, setShowConfirm] = useState(false)
  const [itemToDelete, setItemToDelete] = useState(null)
  
  const [saving, setSaving] = useState(false)
  const [loadError, setLoadError] = useState('')
  const [editingId, setEditingId] = useState(null)
  const [selectedDate, setSelectedDate] = useState(toInputDate(today))
  const [currentMonth, setCurrentMonth] = useState(startOfMonth(today))
  const [viewMode, setViewMode] = useState('mes')
  const [filters, setFilters] = useState({
    Institucional: true,
    Académico: true,
    Actividad: true,
  })

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    watch,
    formState: { errors },
  } = useForm({ defaultValues: initialForm })

  const watchFechaInicio = watch('fechaInicio')
  const watchTipoEvento = watch('tipoEvento')
  const asignaturaNoAplica = tiposSinAsignatura.includes(watchTipoEvento)

  useEffect(() => {
    if (asignaturaNoAplica) setValue('idAsignatura', '')
  }, [asignaturaNoAplica, setValue])

  const loadData = async () => {
    setLoading(true)
    setLoadError('')

    try {
      const [eventosResponse, asignaturasResponse] = await Promise.all([
        getEventos(),
        getAsignaturas(),
      ])

      setEventos(Array.isArray(eventosResponse.data) ? eventosResponse.data : [])
      setAsignaturas(Array.isArray(asignaturasResponse.data) ? asignaturasResponse.data : [])
    } catch (error) {
      console.error(error)
      const message = error.response?.data?.message || 'No se pudo cargar el calendario'
      setLoadError(message)
      toast.error(message)
      setEventos([])
      setAsignaturas([])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  useEffect(() => {
    if (!editingId && selectedDate) {
      setValue('fechaInicio', selectedDate)
      setValue('fechaFin', selectedDate)
    }
  }, [editingId, selectedDate, setValue])

  useEffect(() => {
    if (watchFechaInicio) {
      setSelectedDate(watchFechaInicio)
      const parsed = fromInputDate(watchFechaInicio)
      if (parsed) setCurrentMonth(startOfMonth(parsed))
    }
  }, [watchFechaInicio])

  const normalizedEvents = useMemo(
    () =>
      eventos.map((evento) => ({
        ...evento,
        fechaInicioDate: fromInputDate(evento.fechaInicio),
        fechaFinDate: fromInputDate(evento.fechaFin),
      })),
    [eventos]
  )

  const visibleEvents = useMemo(
    () =>
      normalizedEvents.filter(
        (evento) => filters[toneLabel(evento.tipoEvento)] !== false && eventoVisiblePorAudiencia(evento)
      ),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [filters, normalizedEvents, esDirectivo, esInspector, esDocente, esApoderado, esEstudiante, cursosHijosApoderado, cursoPropioEstudiante, usuario?.usuRut]
  )

  const selectedDateValue = useMemo(() => fromInputDate(selectedDate) || today, [selectedDate, today])

  const selectedDateEvents = useMemo(() => {
    return visibleEvents.filter((evento) => {
      if (!evento.fechaInicioDate || !evento.fechaFinDate) return false
      return isInsideRange(selectedDateValue, evento.fechaInicioDate, evento.fechaFinDate)
    })
  }, [selectedDateValue, visibleEvents])

  const monthCells = useMemo(() => {
    const firstDay = startOfMonth(currentMonth)
    const offset = (firstDay.getDay() + 6) % 7
    const start = addDays(firstDay, -offset)
    return Array.from({ length: 42 }, (_, index) => addDays(start, index))
  }, [currentMonth])

  const weekCells = useMemo(() => {
    const start = startOfWeek(selectedDateValue)
    return Array.from({ length: 7 }, (_, index) => addDays(start, index))
  }, [selectedDateValue])

  const selectedMonthEvents = useMemo(
    () => visibleEvents.filter((evento) => evento.fechaInicioDate && evento.fechaInicioDate.getMonth() === currentMonth.getMonth()),
    [currentMonth, visibleEvents]
  )

  const stats = useMemo(() => ({
    total: visibleEvents.length,
    institucionales: visibleEvents.filter((evento) => toneLabel(evento.tipoEvento) === 'Institucional').length,
    academicos: visibleEvents.filter((evento) => toneLabel(evento.tipoEvento) === 'Académico').length,
    proximos: visibleEvents.filter((evento) => {
      if (!evento.fechaInicioDate) return false
      return evento.fechaInicioDate >= today
    }).length,
  }), [today, visibleEvents])

  const eventLookup = useMemo(() => {
    const map = new Map()
    visibleEvents.forEach((evento) => {
      if (!evento.fechaInicioDate) return
      const key = toInputDate(evento.fechaInicioDate)
      if (!map.has(key)) map.set(key, [])
      map.get(key).push(evento)
    })
    return map
  }, [visibleEvents])

  const handleSelectDay = (date) => {
    const next = toInputDate(date)
    setSelectedDate(next)
    setCurrentMonth(startOfMonth(date))
    if (!editingId) {
      setValue('fechaInicio', next)
      setValue('fechaFin', next)
    }
  }

  const handleCreateOrUpdate = async (data) => {
    setSaving(true)

    const esSinAsignatura = tiposSinAsignatura.includes(data.tipoEvento)

    const payload = {
      tituloEvento: data.tituloEvento,
      tipoEvento: data.tipoEvento,
      fechaInicio: data.fechaInicio,
      fechaFin: data.fechaFin,
      idAsignatura: esSinAsignatura ? null : Number(data.idAsignatura),
      descripcionEvento: data.descripcionEvento,
      idMuralDigital: null,
    }

    try {
      // Institucional/Actividad se reflejan también en el Mural Digital, vinculados al creador.
      if (!editingId && tiposParaMural.includes(data.tipoEvento)) {
        const muralResponse = await crearMural({
          titulo: data.tituloEvento,
          contenido: data.descripcionEvento,
          fechaPublicacion: data.fechaInicio,
          funcionarioUsuRut: usuario?.usuRut ?? null,
        })
        payload.idMuralDigital = muralResponse?.data?.idMurDig ?? null
      }

      if (editingId) {
        await actualizarEvento(editingId, payload)
        toast.success('Evento actualizado')
      } else {
        await crearEvento(payload)
        toast.success('Evento creado')
      }

      reset(initialForm)
      setEditingId(null)
      await loadData()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo guardar el evento')
    } finally {
      setSaving(false)
    }
  }

  const handleEdit = (evento) => {
    setEditingId(evento.idCalEst)
    const fechaInicio = evento.fechaInicio ? toInputDate(fromInputDate(evento.fechaInicio)) : ''
    const fechaFin = evento.fechaFin ? toInputDate(fromInputDate(evento.fechaFin)) : fechaInicio

    reset({
      tituloEvento: evento.tituloEvento || '',
      tipoEvento: evento.tipoEvento || 'Académico',
      fechaInicio,
      fechaFin,
      idAsignatura: evento.idAsignatura ? String(evento.idAsignatura) : '',
      descripcionEvento: evento.descripcionEvento || '',
    })
    if (fechaInicio) {
      setSelectedDate(fechaInicio)
      const parsed = fromInputDate(fechaInicio)
      if (parsed) setCurrentMonth(startOfMonth(parsed))
    }
    document.getElementById('form-evento')?.scrollIntoView({ behavior: 'smooth' })
  }

  // --- NUEVAS FUNCIONES PARA EL MODAL DE ELIMINAR ---
  const clickEliminar = (id) => {
    setItemToDelete(id)
    setShowConfirm(true)
  }

  const confirmarEliminacion = async () => {
    if (!itemToDelete) return
    try {
      await eliminarEvento(itemToDelete)
      toast.success('Evento eliminado')
      await loadData()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo eliminar el evento')
    } finally {
      setShowConfirm(false)
      setItemToDelete(null)
    }
  }
  // ----------------------------------------------------

  const resetForm = () => {
    setEditingId(null)
    reset(initialForm)
    if (selectedDate) {
      setValue('fechaInicio', selectedDate)
      setValue('fechaFin', selectedDate)
    }
  }

  const toggleFilter = (label) => {
    setFilters((current) => ({ ...current, [label]: !current[label] }))
  }

  const renderCalendarGrid = () => (
    <div className={styles.gridWrap}>
      <div className={styles.daysHeader}>
        {['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'].map((day, index) => (
          <div key={day} className={`${styles.dayHeader} ${index >= 5 ? styles.weekend : ''}`}>
            {day}
          </div>
        ))}
      </div>

      <div className={styles.calendarGrid}>
        {monthCells.map((date) => {
          const key = toInputDate(date)
          const eventsForDay = eventLookup.get(key) || []
          const isCurrentMonth = date.getMonth() === currentMonth.getMonth()
          const isSelected = key === selectedDate
          const isToday = isSameDay(date, today)

          return (
            <button
              key={key}
              type="button"
              className={`${styles.dayCell} ${!isCurrentMonth ? styles.dayMuted : ''} ${isSelected ? styles.daySelected : ''} ${isToday ? styles.dayToday : ''}`}
              onClick={() => handleSelectDay(date)}
            >
              <span className={styles.dayNumber}>{date.getDate()}</span>
              <div className={styles.cellEvents}>
                {eventsForDay.slice(0, 2).map((evento) => (
                  <span key={evento.idCalEst} className={`${styles.eventChip} ${styles[eventTone(evento.tipoEvento)]}`}>
                    {evento.tituloEvento}
                  </span>
                ))}
                {eventsForDay.length > 2 ? (
                  <span className={styles.moreChip}>+{eventsForDay.length - 2} eventos</span>
                ) : null}
              </div>
            </button>
          )
        })}
      </div>
    </div>
  )

  const renderWeekView = () => (
    <div className={styles.weekWrap}>
      {weekCells.map((date) => {
        const key = toInputDate(date)
        const eventsForDay = eventLookup.get(key) || []
        return (
          <button key={key} type="button" className={`${styles.weekCard} ${key === selectedDate ? styles.weekCardActive : ''}`} onClick={() => handleSelectDay(date)}>
            <span className={styles.weekDayLabel}>{formatWeekday(date)}</span>
            <strong>{date.getDate()}</strong>
            <span className={styles.weekEventsCount}>{eventsForDay.length} eventos</span>
          </button>
        )
      })}
    </div>
  )

  const renderListView = () => (
    <div className={styles.listView}>
      {visibleEvents.length === 0 ? (
        <div className={styles.emptyState}>No hay eventos para mostrar.</div>
      ) : (
        visibleEvents.map((evento) => (
          <article key={evento.idCalEst} className={styles.listItem}>
            <div>
              <p className={styles.listItemLabel}>{toneLabel(evento.tipoEvento)}</p>
              <h4>{evento.tituloEvento}</h4>
              <p>{evento.descripcionEvento}</p>
            </div>
            <div className={styles.listItemMeta}>
              <span>{formatLongDate(evento.fechaInicio)}</span>
              <span>{evento.idAsignatura ? `Asignatura #${evento.idAsignatura}` : 'No aplica'}</span>
            </div>
          </article>
        ))
      )}
    </div>
  )

  return (
    <div className={styles.page}>
      <main className={styles.shell}>
        <section className={styles.heroCard}>
          <div>
            <h1>Calendario Escolar</h1>
            <div className={styles.heroActions}>
              <Button type="button" variant="outline" onClick={() => loadData()}>
                <FiRefreshCw /> Recargar
              </Button>
              <Button type="button" variant="ghost" onClick={() => handleSelectDay(today)}>
                <FiCalendar /> Ir a hoy
              </Button>
            </div>
          </div>

          <aside className={styles.heroStats}>
            <article>
              <span>Total visible</span>
              <strong>{stats.total}</strong>
            </article>
            <article>
              <span>Académicos</span>
              <strong>{stats.academicos}</strong>
            </article>
            <article>
              <span>Institucionales</span>
              <strong>{stats.institucionales}</strong>
            </article>
            <article>
              <span>Próximos</span>
              <strong>{stats.proximos}</strong>
            </article>
          </aside>
        </section>

        <section className={styles.toolbar}>
          <div className={styles.viewTabs}>
            <button type="button" className={viewMode === 'mes' ? styles.tabActive : styles.tab} onClick={() => setViewMode('mes')}>
              Mes
            </button>
            <button type="button" className={viewMode === 'semana' ? styles.tabActive : styles.tab} onClick={() => setViewMode('semana')}>
              Semana
            </button>
            <button type="button" className={viewMode === 'lista' ? styles.tabActive : styles.tab} onClick={() => setViewMode('lista')}>
              <FiList /> Lista
            </button>
          </div>

          <div className={styles.monthNavigator}>
            <Button type="button" variant="ghost" onClick={() => setCurrentMonth((current) => new Date(current.getFullYear(), current.getMonth() - 1, 1))}>
              <FiChevronLeft />
            </Button>
            <span>{formatMonthLabel(currentMonth)}</span>
            <Button type="button" variant="ghost" onClick={() => setCurrentMonth((current) => new Date(current.getFullYear(), current.getMonth() + 1, 1))}>
              <FiChevronRight />
            </Button>
          </div>

          {puedeGestionar && (
            <Button type="button" variant="primary" onClick={() => document.getElementById('form-evento')?.scrollIntoView({ behavior: 'smooth' })}>
              <FiPlus /> Crear evento
            </Button>
          )}
        </section>

        {loadError ? <div className={styles.errorBanner}>{loadError}</div> : null}

        <div className={styles.layout}>
          <section className={styles.calendarCard}>
            {viewMode === 'mes' ? renderCalendarGrid() : null}
            {viewMode === 'semana' ? renderWeekView() : null}
            {viewMode === 'lista' ? renderListView() : null}
          </section>

          <aside className={styles.sidePanel}>
            <div className={styles.panelCard}>
              <div className={styles.panelHeader}>
                <h3>Filtros</h3>
                <span>{selectedDateEvents.length} eventos</span>
              </div>

              <div className={styles.filterList}>
                {eventTypes.map((type) => (
                  <label key={type} className={styles.filterItem}>
                    <input type="checkbox" checked={filters[type]} onChange={() => toggleFilter(type)} />
                    <span className={`${styles.dot} ${styles[eventTone(type)]}`} />
                    <span>{type}</span>
                  </label>
                ))}
              </div>
            </div>

            <div className={styles.panelCard}>
              <div className={styles.panelHeader}>
                <h3>{formatLongDate(selectedDate)}</h3>
                <button type="button" className={styles.iconButton} onClick={() => handleSelectDay(today)}>
                  <FiClock />
                </button>
              </div>

              <div className={styles.eventDetails}>
                {selectedDateEvents.length === 0 ? (
                  <div className={styles.emptyState}>No hay eventos para esta fecha.</div>
                ) : (
                  selectedDateEvents.map((evento) => (
                    <article key={evento.idCalEst} className={styles.detailCard}>
                      <div className={styles.detailTop}>
                        <h4>{evento.tituloEvento}</h4>
                        <span className={`${styles.badge} ${styles[eventTone(evento.tipoEvento)]}`}>{toneLabel(evento.tipoEvento)}</span>
                      </div>
                      <p>{evento.descripcionEvento}</p>
                      <div className={styles.detailMeta}>
                        <span>{evento.idAsignatura ? `Asignatura #${evento.idAsignatura}` : 'No aplica'}</span>
                        <span>{formatLongDate(evento.fechaInicio)}</span>
                      </div>
                      {puedeEditarEvento(evento) && (
                        <div className={styles.detailActions}>
                          <Button type="button" variant="outline" onClick={() => handleEdit(evento)}>
                            Editar
                          </Button>
                          <Button type="button" variant="danger" onClick={() => clickEliminar(evento.idCalEst)}>
                            Eliminar
                          </Button>
                        </div>
                      )}
                    </article>
                  ))
                )}
              </div>
            </div>
          </aside>
        </div>

        {puedeGestionar && (
        <section className={styles.formSection} id="form-evento">
          <div className={styles.formCard}>
            <div className={styles.formHeader}>
              <div>
                <p className={styles.eyebrow}>Formulario</p>
                <h2>{editingId ? `Editar evento #${editingId}` : 'Nuevo evento'}</h2>
              </div>
              {editingId ? (
                <Button type="button" variant="outline" onClick={resetForm}>
                  Cancelar edición
                </Button>
              ) : null}
            </div>

            <form className={styles.formGrid} onSubmit={handleSubmit(handleCreateOrUpdate)}>
              <Input
                label="Título del evento"
                type="text"
                placeholder="Ej. Prueba de Matemáticas"
                error={errors.tituloEvento?.message}
                {...register('tituloEvento', { required: 'El título es obligatorio' })}
              />

              <label className={styles.field}>
                <span>Tipo de evento</span>
                <select className={styles.select} disabled={esDocente} {...register('tipoEvento', { required: 'El tipo es obligatorio' })}>
                  {(esDocente ? ['Académico'] : eventTypes).map((type) => (
                    <option key={type} value={type}>
                      {type}
                    </option>
                  ))}
                </select>
                {errors.tipoEvento ? <span className={styles.errorText}>{errors.tipoEvento.message}</span> : null}
              </label>

              <Input
                label="Fecha inicio"
                type="date"
                error={errors.fechaInicio?.message}
                {...register('fechaInicio', { required: 'La fecha de inicio es obligatoria' })}
              />

              <Input
                label="Fecha fin"
                type="date"
                error={errors.fechaFin?.message}
                {...register('fechaFin', { required: 'La fecha de fin es obligatoria' })}
              />

              <label className={styles.field}>
                <span>Asignatura</span>
                {asignaturaNoAplica ? (
                  <select className={styles.select} value="no-aplica" disabled>
                    <option value="no-aplica">No aplica</option>
                  </select>
                ) : (
                  <select
                    className={styles.select}
                    {...register('idAsignatura', { required: 'La asignatura es obligatoria' })}
                  >
                    <option value="">Selecciona una asignatura</option>
                    {asignaturasParaForm.map((asignatura) => (
                      <option key={asignatura.idAsi} value={asignatura.idAsi}>
                        {asignatura.asiNombre}
                      </option>
                    ))}
                  </select>
                )}
                {errors.idAsignatura ? <span className={styles.errorText}>{errors.idAsignatura.message}</span> : null}
              </label>

              <label className={`${styles.field} ${styles.fullWidth}`}>
                <span>Descripción</span>
                <textarea
                  className={styles.textarea}
                  rows="5"
                  placeholder="Describe el evento, público objetivo o información relevante"
                  {...register('descripcionEvento', { required: 'La descripción es obligatoria' })}
                />
                {errors.descripcionEvento ? <span className={styles.errorText}>{errors.descripcionEvento.message}</span> : null}
              </label>

              <div className={styles.formActions}>
                <Button type="submit" disabled={saving}>
                  {saving ? 'Guardando...' : editingId ? 'Actualizar evento' : 'Crear evento'}
                </Button>
                <Button type="button" variant="ghost" onClick={resetForm}>
                  Limpiar formulario
                </Button>
              </div>
            </form>
          </div>

          <div className={styles.listCard}>
            <div className={styles.formHeader}>
              <div>
                <p className={styles.eyebrow}>Agenda visible</p>
                <h2>Eventos del mes</h2>
              </div>
              <span className={styles.counter}>{selectedMonthEvents.length} registros</span>
            </div>

            {loading ? (
              <div className={styles.emptyState}>Cargando calendario...</div>
            ) : selectedMonthEvents.length === 0 ? (
              <div className={styles.emptyState}>No hay eventos para mostrar en este mes.</div>
            ) : (
              <div className={styles.eventList}>
                {selectedMonthEvents.map((evento) => (
                  <article key={evento.idCalEst} className={styles.eventRow}>
                    <div>
                      <span className={`${styles.badge} ${styles[eventTone(evento.tipoEvento)]}`}>{toneLabel(evento.tipoEvento)}</span>
                      <h4>{evento.tituloEvento}</h4>
                      <p>{evento.descripcionEvento}</p>
                    </div>
                    <div className={styles.rowActions}>
                      <span>{formatLongDate(evento.fechaInicio)}</span>
                      {puedeEditarEvento(evento) && (
                        <div className={styles.rowButtons}>
                          <Button type="button" variant="outline" onClick={() => handleEdit(evento)}>
                            Editar
                          </Button>
                          <Button type="button" variant="danger" onClick={() => clickEliminar(evento.idCalEst)}>
                            Eliminar
                          </Button>
                        </div>
                      )}
                    </div>
                  </article>
                ))}
              </div>
            )}
          </div>
        </section>
        )}
      </main>

      {/* --- MODAL DE CONFIRMACIÓN DE ELIMINACIÓN --- */}
      <Modal show={showConfirm} onHide={() => setShowConfirm(false)} centered>
        <Modal.Header closeButton className={styles.modalHeader}>
          <Modal.Title className={styles.modalTitle}>
            Confirmar eliminación
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p className="mb-0 fs-5 text-dark">
            ¿Estás seguro de que deseas eliminar este evento del calendario?
          </p>
          <p className="text-muted small mt-2 mb-0">
            Esta acción no se puede deshacer.
          </p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="outline" onClick={() => setShowConfirm(false)}>
            Cancelar
          </Button>
          <Button variant="danger" onClick={confirmarEliminacion}>
            Sí, eliminar
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  )
}