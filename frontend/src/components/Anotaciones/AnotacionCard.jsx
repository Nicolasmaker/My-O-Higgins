import Button from '../UI/Button'
import { formatRut } from '../../utils/formatRut'
import '../../styles/AnotacionCard.css'

function labelRolFuncionario(rol) {
  const rolNormalizado = String(rol || '').toUpperCase()
  if (rolNormalizado.includes('INSPECTOR')) return 'Inspector'
  if (rolNormalizado.includes('DOCENTE')) return 'Docente'
  if (rolNormalizado.includes('DIRECTIVO')) return 'Directivo'
  return 'Funcionario'
}

export default function AnotacionCard({ anotacion, onEdit, onDelete, onVincularCitacion, formatDate, canManage }) {
  const rutFuncionario = formatRut(anotacion.funcionarioUsuRut, anotacion.funcionarioDv)
  const rutEstudiante = formatRut(anotacion.estudianteUsuRut, anotacion.estudianteDv)

  return (
    <article className="anotacion-card">
      <div className="anotacion-card__top">
        <span className={`anotacion-card__badge ${String(anotacion.anotTip || '').toLowerCase()}`}>
          {anotacion.anotTip}
        </span>
        {anotacion.anotTip === 'Negativa' && anotacion.anotGravedad && (
          <span
            className={`anotacion-card__badge gravedad-${anotacion.anotGravedad.toLowerCase().replace(/\s+/g, '-')}`}
          >
            {anotacion.anotGravedad}
          </span>
        )}
        <span className="anotacion-card__date">{formatDate(anotacion.anotFec)}</span>
      </div>

      <p className="anotacion-card__description">{anotacion.anotDes}</p>

      <dl className="anotacion-card__meta">
        <div>
          <dt>ID</dt>
          <dd>{anotacion.idAnot}</dd>
        </div>
        <div>
          <dt>{labelRolFuncionario(anotacion.funcionarioRol)}</dt>
          <dd>
            {anotacion.funcionarioNombre
              ? `${anotacion.funcionarioNombre} ${anotacion.funcionarioApellido || ''}`.trim()
              : '—'}
            {rutFuncionario && (
              <span className="anotacion-card__rut">
                <small>Rut</small> {rutFuncionario}
              </span>
            )}
          </dd>
        </div>
        <div>
          <dt>Estudiante</dt>
          <dd>
            {anotacion.estudianteNombre
              ? `${anotacion.estudianteNombre} ${anotacion.estudianteApellido || ''}`.trim()
              : '—'}
            {rutEstudiante && (
              <span className="anotacion-card__rut">
                <small>Rut</small> {rutEstudiante}
              </span>
            )}
          </dd>
        </div>
        <div>
          <dt>Curso</dt>
          <dd>{anotacion.curso || '—'}</dd>
        </div>
      </dl>

      {(canManage || (anotacion.anotTip === 'Negativa' && onVincularCitacion)) && (
        <div className="anotacion-card__actions">
          {canManage && (
            <>
              <Button type="button" variant="outline" onClick={() => onEdit(anotacion)}>
                Editar
              </Button>
              <Button type="button" variant="danger" onClick={() => onDelete(anotacion.idAnot)}>
                Eliminar
              </Button>
            </>
          )}
          {anotacion.anotTip === 'Negativa' && onVincularCitacion && (
            <Button type="button" variant="outline" onClick={() => onVincularCitacion(anotacion)}>
              Vincular citación
            </Button>
          )}
        </div>
      )}
    </article>
  )
}
