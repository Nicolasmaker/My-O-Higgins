import Button from '../UI/Button'
import './AnotacionCard.css'

export default function AnotacionCard({ anotacion, onEdit, onDelete, formatDate, canManage }) {
  return (
    <article className="anotacion-card">
      <div className="anotacion-card__top">
        <span className={`anotacion-card__badge ${String(anotacion.anotTip || '').toLowerCase()}`}>
          {anotacion.anotTip}
        </span>
        <span className="anotacion-card__date">{formatDate(anotacion.anotFec)}</span>
      </div>

      <p className="anotacion-card__description">{anotacion.anotDes}</p>

      <dl className="anotacion-card__meta">
        <div>
          <dt>ID</dt>
          <dd>{anotacion.idAnot}</dd>
        </div>
        <div>
          <dt>Funcionario</dt>
          <dd>{anotacion.funcionarioUsuRut}</dd>
        </div>
      </dl>

      {canManage && (
        <div className="anotacion-card__actions">
          <Button type="button" variant="outline" onClick={() => onEdit(anotacion)}>
            Editar
          </Button>
          <Button type="button" variant="danger" onClick={() => onDelete(anotacion.idAnot)}>
            Eliminar
          </Button>
        </div>
      )}
    </article>
  )
}
