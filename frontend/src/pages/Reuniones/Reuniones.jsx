// =============================================================
// PÁGINA DE REUNIONES — Reuniones.jsx
// =============================================================
// Interfaz del MS-GestionReuniones (puerto 8081). Tres tipos de
// registro, cada uno en su pestaña:
//
//   - Con Apoderado → bitácora base (RF29). Editable: compromisos/obs.
//   - Individual    → entrevista confidencial + estado de firmas.
//   - General       → acta de coordinación institucional. Solo lectura.
//
// El formulario de creación aparece plegado; se abre con el botón
// "Nueva reunión" y siempre crea sobre la pestaña activa.
// =============================================================
import { useCallback, useEffect, useMemo, useState } from 'react'
import { Tabs, Tab, Row, Col, Alert, Spinner, Button, ButtonGroup } from 'react-bootstrap'
import { toast } from 'react-toastify'
import { useAuth } from '../../hooks/useAuth'
import {
  getReunionesApoderado,
  getReunionesApoderadoPorFuncionario,
  crearReunionApoderado,
  actualizarReunionApoderado,
  getReunionesIndividuales,
  crearReunionIndividual,
  actualizarFirmasIndividual,
  getReunionesGenerales,
  crearReunionGeneral,
} from '../../services/reunionesService'
import Navbar from '../../components/Navbar/Navbar'
import Footer from '../../components/Footer/Footer'
import ReunionForm from '../../components/Reuniones/ReunionForm'
import ReunionApoderadoCard from '../../components/Reuniones/ReunionApoderadoCard'
import ReunionIndividualCard from '../../components/Reuniones/ReunionIndividualCard'
import ReunionGeneralCard from '../../components/Reuniones/ReunionGeneralCard'
import styles from './Reuniones.module.css'

const ROLES_GESTION = ['ROLE_DOCENTE', 'ROLE_INSPECTOR', 'ROLE_DIRECTIVO']

function formatDate(value) {
  if (!value) return 'Sin fecha'
  return new Date(`${value}T00:00:00`).toLocaleDateString('es-CL', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  })
}

export default function Reuniones() {
  const { usuario, hasRole } = useAuth()

  const [apoderado, setApoderado] = useState([])
  const [individuales, setIndividuales] = useState([])
  const [generales, setGenerales] = useState([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')

  const [activeTab, setActiveTab] = useState('apoderado')
  const [showForm, setShowForm] = useState(false)
  const [saving, setSaving] = useState(false)
  const [soloMias, setSoloMias] = useState(false)

  const userRut = usuario?.usuRut ?? ''
  const canManage = hasRole(ROLES_GESTION)

  const loadAll = useCallback(async (filtrarPorRut = false, rut = '') => {
    setLoading(true)
    setLoadError('')
    try {
      const [apoRes, indRes, genRes] = await Promise.all([
        filtrarPorRut && rut ? getReunionesApoderadoPorFuncionario(rut) : getReunionesApoderado(),
        getReunionesIndividuales(),
        getReunionesGenerales(),
      ])
      setApoderado(Array.isArray(apoRes.data) ? apoRes.data : [])
      setIndividuales(Array.isArray(indRes.data) ? indRes.data : [])
      setGenerales(Array.isArray(genRes.data) ? genRes.data : [])
    } catch (error) {
      console.error(error)
      const message = error.response?.data?.message || 'No se pudieron cargar las reuniones'
      setLoadError(message)
      toast.error(message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    loadAll(soloMias, userRut)
  }, [loadAll, soloMias, userRut])

  const counts = useMemo(
    () => ({ apoderado: apoderado.length, individual: individuales.length, general: generales.length }),
    [apoderado, individuales, generales]
  )

  // ── Crear según pestaña activa ──────────────────────────────
  const handleCreate = async (data) => {
    setSaving(true)
    const base = {
      bitReuFec: data.bitReuFec,
      bitReuCompromisos: data.bitReuCompromisos,
      bitReuObs: data.bitReuObs || null,
      docenteUsuRut: Number(data.docenteUsuRut),
    }

    try {
      if (activeTab === 'individual') {
        await crearReunionIndividual({
          ...base,
          bitReuIndMotivReu: data.bitReuIndMotivReu,
          bitReuIndTemTrat: data.bitReuIndTemTrat,
        })
      } else if (activeTab === 'general') {
        await crearReunionGeneral({
          ...base,
          bitReuGenTipReu: data.bitReuGenTipReu,
          bitReuGenComunicEmi: data.bitReuGenComunicEmi,
          bitReuGenAcuerTrat: data.bitReuGenAcuerTrat,
          bitReuGenObs: data.bitReuGenObs || null,
        })
      } else {
        await crearReunionApoderado(base)
      }
      toast.success('Reunión registrada')
      setShowForm(false)
      await loadAll(soloMias, userRut)
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || error.response?.data || 'No se pudo registrar la reunión')
    } finally {
      setSaving(false)
    }
  }

  // ── Corregir compromisos/obs de bitácora base ───────────────
  const handleUpdateApoderado = async (id, payload) => {
    try {
      await actualizarReunionApoderado(id, payload)
      toast.success('Bitácora actualizada')
      await loadAll(soloMias, userRut)
      return true
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo actualizar la bitácora')
      return false
    }
  }

  // ── Actualizar firmas de entrevista individual ──────────────
  const handleFirmar = async (id, payload) => {
    try {
      await actualizarFirmasIndividual(id, payload)
      toast.success('Firma registrada')
      await loadAll(soloMias, userRut)
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo registrar la firma')
    }
  }

  const renderGrid = (items, getKey, renderCard, emptyMessage) => {
    if (loading) {
      return (
        <div className={styles.emptyState}>
          <Spinner animation="border" size="sm" className="me-2" />
          Cargando reuniones...
        </div>
      )
    }
    if (loadError) return <Alert variant="danger">{loadError}</Alert>
    if (items.length === 0) return <div className={styles.emptyState}>{emptyMessage}</div>
    return (
      <Row xs={1} md={2} className="g-3">
        {items.map((item) => (
          <Col key={getKey(item)}>{renderCard(item)}</Col>
        ))}
      </Row>
    )
  }

  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.shell}>
        {/* ── Encabezado compacto ── */}
        <header className={styles.pageHeader}>
          <div>
            <p className={styles.eyebrow}>MS-GestionReuniones</p>
            <h1 className={styles.title}>Bitácora de Reuniones</h1>
            <p className={styles.subtitle}>Registro institucional de reuniones con apoderados — Colegio Bernardo O&apos;Higgins</p>
          </div>
          <div className={styles.headerActions}>
            {usuario && (
              <span className={styles.userChip}>
                {`${usuario.usuPNombre || ''} ${usuario.usuApePat || ''}`.trim() || 'Sesión activa'}
                {usuario.rolNombre ? ` · ${usuario.rolNombre.replace('ROLE_', '')}` : ''}
              </span>
            )}
            {canManage && (
              <Button className={styles.btnGranate} onClick={() => setShowForm((v) => !v)}>
                {showForm ? 'Cerrar formulario' : '+ Nueva reunión'}
              </Button>
            )}
          </div>
        </header>

        {/* ── Formulario plegable (crea sobre la pestaña activa) ── */}
        {showForm && canManage && (
          <ReunionForm
            key={activeTab}
            tipo={activeTab}
            defaultRut={userRut}
            saving={saving}
            onSubmit={handleCreate}
            onCancel={() => setShowForm(false)}
          />
        )}

        {/* ── Pestañas por tipo de reunión ── */}
        <Tabs
          activeKey={activeTab}
          onSelect={(key) => setActiveTab(key)}
          className={styles.tabs}
          transition={false}
        >
          <Tab eventKey="apoderado" title={`Con Apoderado (${counts.apoderado})`}>
            <div className={styles.tabToolbar}>
              <span className="text-muted">{counts.apoderado} bitácoras</span>
              <ButtonGroup size="sm">
                <Button
                  variant={soloMias ? 'outline-secondary' : 'secondary'}
                  onClick={() => setSoloMias(false)}
                >
                  Todas
                </Button>
                <Button
                  variant={soloMias ? 'secondary' : 'outline-secondary'}
                  disabled={!userRut}
                  onClick={() => setSoloMias(true)}
                >
                  Mis reuniones
                </Button>
              </ButtonGroup>
            </div>
            {renderGrid(
              apoderado,
              (item) => item.idBitReu,
              (item) => (
                <ReunionApoderadoCard
                  reunion={item}
                  formatDate={formatDate}
                  onUpdate={handleUpdateApoderado}
                  canEdit={canManage}
                />
              ),
              'No hay bitácoras de reuniones con apoderado.'
            )}
          </Tab>

          <Tab eventKey="individual" title={`Individual (${counts.individual})`}>
            {renderGrid(
              individuales,
              (item) => item.idBitReuInd,
              (item) => (
                <ReunionIndividualCard
                  reunion={item}
                  formatDate={formatDate}
                  onFirmar={handleFirmar}
                  canEdit={canManage}
                />
              ),
              'No hay entrevistas individuales registradas.'
            )}
          </Tab>

          <Tab eventKey="general" title={`General (${counts.general})`}>
            {renderGrid(
              generales,
              (item) => item.bitReuGen,
              (item) => (
                <ReunionGeneralCard reunion={item} formatDate={formatDate} />
              ),
              'No hay actas de reuniones generales.'
            )}
          </Tab>
        </Tabs>
      </main>

      <Footer />
    </div>
  )
}
