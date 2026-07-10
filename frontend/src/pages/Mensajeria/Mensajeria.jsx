// =============================================================
// PÁGINA DE MENSAJERÍA — Mensajeria.jsx
// =============================================================
// Bandeja de correo interno del MS-Mensajeria (puerto 8089).
// Layout tipo cliente de correo: lista de mensajes a la izquierda,
// panel de lectura a la derecha.
//
//   - Recibidos → GET /bandeja/{rut}. Al abrir un mensaje no leído
//     se marca leído automáticamente (RF-22).
//   - Enviados  → GET /enviados/{rut}. Solo lectura + eliminar.
//   - Redactar  → modal; remitente sale de la sesión activa.
// =============================================================
import { useCallback, useEffect, useMemo, useState } from 'react'
import { Row, Col, Nav, ListGroup, Badge, Button, Alert, Spinner } from 'react-bootstrap'
import { toast } from 'react-toastify'
import { useAuth } from '../../hooks/useAuth'
import { limpiarRut } from '../../validators/fieldValidators'
import {
  getBandeja,
  getEnviados,
  enviarMensaje,
  marcarComoLeido,
  eliminarMensaje,
} from '../../services/mensajeriaService'
import MensajeCompose from '../../components/Mensajeria/MensajeCompose'
import styles from '../../styles/Mensajeria.module.css'

function formatDate(value) {
  if (!value) return ''
  return new Date(`${value}T00:00:00`).toLocaleDateString('es-CL', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  })
}

export default function Mensajeria() {
  const { usuario, isAuthenticated } = useAuth()
  const userRut = usuario?.usuRut ?? null

  const [folder, setFolder] = useState('recibidos')
  const [recibidos, setRecibidos] = useState([])
  const [enviados, setEnviados] = useState([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')
  const [selectedId, setSelectedId] = useState(null)
  const [showCompose, setShowCompose] = useState(false)
  const [sending, setSending] = useState(false)

  const loadAll = useCallback(async () => {
    if (!userRut) {
      setLoading(false)
      return
    }
    setLoading(true)
    setLoadError('')
    try {
      const [inRes, outRes] = await Promise.all([getBandeja(userRut), getEnviados(userRut)])
      setRecibidos(Array.isArray(inRes.data) ? inRes.data : [])
      setEnviados(Array.isArray(outRes.data) ? outRes.data : [])
    } catch (error) {
      console.error(error)
      const message = error.response?.data?.message || 'No se pudieron cargar los mensajes'
      setLoadError(message)
      toast.error(message)
    } finally {
      setLoading(false)
    }
  }, [userRut])

  useEffect(() => {
    loadAll()
  }, [loadAll])

  const mensajes = folder === 'recibidos' ? recibidos : enviados
  const noLeidos = useMemo(() => recibidos.filter((m) => !m.estadoLectura).length, [recibidos])
  const selected = mensajes.find((m) => m.idMensaje === selectedId) || null

  // Al abrir un mensaje recibido no leído, se marca leído (RF-22)
  const handleSelect = async (mensaje) => {
    setSelectedId(mensaje.idMensaje)
    if (folder === 'recibidos' && !mensaje.estadoLectura) {
      try {
        await marcarComoLeido(mensaje.idMensaje)
        setRecibidos((prev) =>
          prev.map((m) => (m.idMensaje === mensaje.idMensaje ? { ...m, estadoLectura: true } : m))
        )
      } catch (error) {
        console.error(error)
        // no bloquea la lectura; solo no se actualizó el estado
      }
    }
  }

  const handleSend = async (data) => {
    setSending(true)
    try {
      await enviarMensaje({
        remitenteRut: Number(userRut),
        destinatarioRut: limpiarRut(data.destinatarioRut),
        asunto: data.asunto,
        contenido: data.contenido,
      })
      toast.success('Mensaje enviado')
      setShowCompose(false)
      await loadAll()
      return true
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || error.response?.data || 'No se pudo enviar el mensaje')
      return false
    } finally {
      setSending(false)
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('¿Eliminar este mensaje?')) return
    try {
      await eliminarMensaje(id)
      toast.success('Mensaje eliminado')
      if (selectedId === id) setSelectedId(null)
      await loadAll()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo eliminar el mensaje')
    }
  }

  const changeFolder = (key) => {
    setFolder(key)
    setSelectedId(null)
  }

  return (
    <div className={styles.page}>
      <main className={styles.shell}>
        <header className={styles.pageHeader}>
          <div>
          
            <h1 className={styles.title}>Mensajería interna</h1>

          </div>
          <Button className={styles.btnGranate} onClick={() => setShowCompose(true)} disabled={!userRut}>
            + Redactar
          </Button>
        </header>

        {!isAuthenticated && (
          <Alert variant="warning">Debes iniciar sesión para usar la mensajería.</Alert>
        )}

        <Nav variant="pills" activeKey={folder} onSelect={changeFolder} className={styles.folderNav}>
          <Nav.Item>
            <Nav.Link eventKey="recibidos" className={styles.folderPill}>
              Recibidos
              {noLeidos > 0 && (
                <Badge bg="danger" pill className="ms-2">
                  {noLeidos}
                </Badge>
              )}
            </Nav.Link>
          </Nav.Item>
          <Nav.Item>
            <Nav.Link eventKey="enviados" className={styles.folderPill}>
              Enviados ({enviados.length})
            </Nav.Link>
          </Nav.Item>
        </Nav>

        {loading ? (
          <div className={styles.emptyState}>
            <Spinner animation="border" size="sm" className="me-2" />
            Cargando mensajes...
          </div>
        ) : loadError ? (
          <Alert variant="danger">{loadError}</Alert>
        ) : mensajes.length === 0 ? (
          <div className={styles.emptyState}>
            {folder === 'recibidos' ? 'No tienes mensajes recibidos.' : 'No has enviado mensajes.'}
          </div>
        ) : (
          <Row className="g-3">
            {/* ── Lista de mensajes ── */}
            <Col md={5} lg={4}>
              <ListGroup className={styles.mailList}>
                {mensajes.map((m) => {
                  const unread = folder === 'recibidos' && !m.estadoLectura
                  return (
                    <ListGroup.Item
                      key={m.idMensaje}
                      action
                      active={m.idMensaje === selectedId}
                      onClick={() => handleSelect(m)}
                      className={`${styles.mailItem} ${unread ? styles.mailItemUnread : ''}`}
                    >
                      <div className={styles.mailItemTop}>
                        <span className={styles.mailRut}>
                          {folder === 'recibidos' ? `De: ${m.remitenteRut}` : `Para: ${m.destinatarioRut}`}
                        </span>
                        <small>{formatDate(m.fechaEnvio)}</small>
                      </div>
                      <div className={styles.mailSubject}>
                        {unread && <span className={styles.unreadDot} />}
                        {m.asunto}
                      </div>
                    </ListGroup.Item>
                  )
                })}
              </ListGroup>
            </Col>

            {/* ── Panel de lectura ── */}
            <Col md={7} lg={8}>
              {selected ? (
                <article className={styles.readPane}>
                  <header className={styles.readHeader}>
                    <div>
                      <h2 className={styles.readSubject}>{selected.asunto}</h2>
                      <div className={styles.readMeta}>
                        <span>
                          {folder === 'recibidos'
                            ? `De: ${selected.remitenteRut}`
                            : `Para: ${selected.destinatarioRut}`}
                        </span>
                        <span>·</span>
                        <span>{formatDate(selected.fechaEnvio)}</span>
                        {folder === 'recibidos' && (
                          <Badge bg={selected.estadoLectura ? 'success' : 'warning'} text={selected.estadoLectura ? undefined : 'dark'}>
                            {selected.estadoLectura ? 'Leído' : 'No leído'}
                          </Badge>
                        )}
                      </div>
                    </div>
                    <Button size="sm" variant="outline-danger" onClick={() => handleDelete(selected.idMensaje)}>
                      Eliminar
                    </Button>
                  </header>
                  <p className={styles.readBody}>{selected.contenido}</p>
                </article>
              ) : (
                <div className={styles.emptyState}>Selecciona un mensaje para leerlo.</div>
              )}
            </Col>
          </Row>
        )}
      </main>

      <MensajeCompose
        show={showCompose}
        sending={sending}
        onSend={handleSend}
        onClose={() => setShowCompose(false)}
      />
    </div>
  )
}
