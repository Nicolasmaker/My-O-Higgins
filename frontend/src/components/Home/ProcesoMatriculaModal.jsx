import { useState } from 'react';
import PropTypes from 'prop-types';
import { Modal, Button, Form } from 'react-bootstrap';
import styles from './ProcesoMatriculaModal.module.css';

const PASOS = ['Requisitos', 'Documentos', 'Datos personales', 'Confirmación'];

const REQUISITOS = [
  'Ser residente en la comuna de Coquimbo o alrededores.',
  'Cumplir con la edad mínima correspondiente al nivel al que postula.',
  'No tener matrícula vigente en otro establecimiento durante el mismo período.',
];

const DOCUMENTOS = [
  'Certificado de nacimiento (original, no mayor a 30 días).',
  'Informe de notas y certificado de alumno regular del colegio anterior (si aplica).',
  'Fotocopia de cédula de identidad del alumno y del apoderado.',
  'Comprobante de domicilio.',
  'Certificado de vacunas al día (educación básica).',
];

export default function ProcesoMatriculaModal({ show, onClose }) {
  const [paso, setPaso] = useState(0);
  const [datos, setDatos] = useState({
    nombre: '',
    rut: '',
    cursoPostula: '',
    contacto: '',
  });

  const esUltimoPaso = paso === PASOS.length - 1;
  const esPrimerPaso = paso === 0;

  const handleClose = () => {
    setPaso(0);
    setDatos({ nombre: '', rut: '', cursoPostula: '', contacto: '' });
    onClose();
  };

  const handleChange = (campo) => (e) => {
    setDatos((prev) => ({ ...prev, [campo]: e.target.value }));
  };

  return (
    <Modal show={show} onHide={handleClose} centered size="lg">
      <Modal.Header closeButton className={styles.header}>
        <Modal.Title className={styles.title}>Proceso de matrícula</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div className={styles.pasos}>
          {PASOS.map((label, i) => (
            <div
              key={label}
              className={`${styles.paso} ${i === paso ? styles.pasoActivo : ''} ${i < paso ? styles.pasoCompletado : ''}`}
            >
              <span className={styles.pasoNumero}>{i + 1}</span>
              <span className={styles.pasoLabel}>{label}</span>
            </div>
          ))}
        </div>

        {paso === 0 && (
          <div>
            <h5>Requisitos de postulación</h5>
            <ul>
              {REQUISITOS.map((r) => (
                <li key={r}>{r}</li>
              ))}
            </ul>
          </div>
        )}

        {paso === 1 && (
          <div>
            <h5>Documentos que debes preparar</h5>
            <ul>
              {DOCUMENTOS.map((d) => (
                <li key={d}>{d}</li>
              ))}
            </ul>
          </div>
        )}

        {paso === 2 && (
          <div>
            <h5>Datos personales</h5>
            <p className="text-muted small">
              Este formulario es solo referencial para que sepas qué información se te pedirá — no se envía a ninguna parte todavía.
            </p>
            <Form>
              <Form.Group className="mb-3">
                <Form.Label>Nombre completo del postulante</Form.Label>
                <Form.Control value={datos.nombre} onChange={handleChange('nombre')} placeholder="Nombre y apellidos" />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>RUT del postulante</Form.Label>
                <Form.Control value={datos.rut} onChange={handleChange('rut')} placeholder="12.345.678-9" />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Curso al que postula</Form.Label>
                <Form.Control value={datos.cursoPostula} onChange={handleChange('cursoPostula')} placeholder="Ej: 1° Básico" />
              </Form.Group>
              <Form.Group>
                <Form.Label>Contacto del apoderado (correo o teléfono)</Form.Label>
                <Form.Control value={datos.contacto} onChange={handleChange('contacto')} placeholder="correo@ejemplo.cl o +56 9..." />
              </Form.Group>
            </Form>
          </div>
        )}

        {paso === 3 && (
          <div className={styles.confirmacion}>
            <h5>¡Listo!</h5>
            <p>
              Una vez que tengas los documentos y datos reunidos, acércate a inspectoría o contacta al colegio para
              formalizar tu matrícula. Un funcionario del colegio se pondrá en contacto contigo para continuar el proceso.
            </p>
          </div>
        )}
      </Modal.Body>

      <Modal.Footer>
        <Button variant="outline-secondary" onClick={() => setPaso((p) => p - 1)} disabled={esPrimerPaso}>
          Anterior
        </Button>
        {esUltimoPaso ? (
          <Button className={styles.btnGranate} onClick={handleClose}>
            Cerrar
          </Button>
        ) : (
          <Button className={styles.btnGranate} onClick={() => setPaso((p) => p + 1)}>
            Siguiente
          </Button>
        )}
      </Modal.Footer>
    </Modal>
  );
}

ProcesoMatriculaModal.propTypes = {
  show: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
};
