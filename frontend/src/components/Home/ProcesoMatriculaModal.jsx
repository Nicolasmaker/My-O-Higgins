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

const DATOS_PERSONALES = [
  'Nombre completo del postulante.',
  'RUT del postulante.',
  'Curso al que postula.',
  'Información de contacto del apoderado (correo electrónico y teléfono).',
];

export default function ProcesoMatriculaModal({ show, onClose }) {
  const [paso, setPaso] = useState(0);

  const esUltimoPaso = paso === PASOS.length - 1;
  const esPrimerPaso = paso === 0;

  const handleClose = () => {
    setPaso(0);
    onClose();
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
            <h5>Datos personales requeridos</h5>
            <p className="text-muted small">
              Asegúrate de tener a mano los siguientes datos para cuando realices el proceso:
            </p>
            <ul>
              {DATOS_PERSONALES.map((dp) => (
                <li key={dp}>{dp}</li>
              ))}
            </ul>
            
            <hr className="my-3" />
            
            <p className="text-muted small bg-light p-3 rounded border">
              <strong>Nota:</strong> El proceso de matrícula se puede realizar de forma presencial 
              directamente en el establecimiento, o bien de manera telefónica comunicándote al 
              número <strong>+56 51 231 3192</strong>.
            </p>
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
