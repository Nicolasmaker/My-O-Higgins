import Button from '../UI/Button'
import Input from '../UI/Input'
import { anotacionRules } from '../../validators/fieldValidators'
import './AnotacionForm.css'

const ESTADO_HOJA_VIDA = {
  idle: null,
  'formato-invalido': {
    text: 'Ingresa el RUT completo, con guion y dígito verificador (ej. 12345678-5).',
    className: 'no-encontrada',
  },
  buscando: { text: 'Buscando hoja de vida...', className: 'buscando' },
  encontrada: { text: null, className: 'encontrada' }, // el texto se arma en el componente con el id
  'no-encontrada': { text: 'Este estudiante no tiene hoja de vida registrada.', className: 'no-encontrada' },
}

export default function AnotacionForm({
  register,
  errors,
  isSaving,
  editingId,
  onSubmit,
  onCancel,
  hojaVidaStatus = 'idle',
  idHojaVidaResuelto = null,
  tipoSeleccionado = 'Positiva',
}) {
  const estado = ESTADO_HOJA_VIDA[hojaVidaStatus]
  const puedeGuardar = editingId
    ? !isSaving
    : !isSaving && hojaVidaStatus === 'encontrada' && idHojaVidaResuelto

  return (
    <form className="anotacion-form" onSubmit={onSubmit}>
      <div className="anotacion-form__header">
        <div>
          <p className="eyebrow">Formulario</p>
          <h2>{editingId ? `Editar anotación #${editingId}` : 'Nueva anotación'}</h2>
        </div>
        {editingId ? (
          <Button type="button" variant="outline" onClick={onCancel}>
            Cancelar edición
          </Button>
        ) : null}
      </div>

      {!editingId && (
        <>
          <Input
            label="RUT del estudiante"
            type="text"
            placeholder="12345678-5"
            error={errors.rutEstudiante?.message}
            {...register('rutEstudiante', anotacionRules.rutEstudiante)}
          />

          {estado && (
            <p className={`anotacion-form__hoja-vida-status ${estado.className}`}>
              {hojaVidaStatus === 'encontrada'
                ? `Hoja de vida encontrada (#${idHojaVidaResuelto}). La anotación se registrará ahí.`
                : estado.text}
            </p>
          )}
        </>
      )}

      <label className="ui-field">
        <span className="ui-field__label">Tipo</span>
        <select className="ui-input" {...register('anotTip', anotacionRules.anotTip)}>
          <option value="Positiva">Positiva</option>
          <option value="Negativa">Negativa</option>
        </select>
        {errors.anotTip ? <span className="ui-field__error">{errors.anotTip.message}</span> : null}
      </label>

      {tipoSeleccionado === 'Negativa' && (
        <label className="ui-field">
          <span className="ui-field__label">Gravedad</span>
          <select className="ui-input" {...register('anotGravedad')}>
            <option value="">Selecciona una gravedad</option>
            <option value="Leve">Leve</option>
            <option value="Grave">Grave</option>
            <option value="Muy Grave">Muy Grave</option>
          </select>
        </label>
      )}

      <label className="ui-field">
        <span className="ui-field__label">Descripción</span>
        <textarea
          className="ui-input"
          rows="6"
          placeholder="Escribe la observación, falta, logro o comportamiento..."
          {...register('anotDes', anotacionRules.anotDes)}
        />
        {errors.anotDes ? <span className="ui-field__error">{errors.anotDes.message}</span> : null}
      </label>

      <Button type="submit" disabled={!puedeGuardar}>
        {isSaving ? 'Guardando...' : editingId ? 'Actualizar anotación' : 'Crear anotación'}
      </Button>
    </form>
  )
}
