import Button from '../UI/Button'
import Input from '../UI/Input'
import { anotacionRules } from '../../validators/fieldValidators'
import './AnotacionForm.css'

export default function AnotacionForm({
  register,
  errors,
  isSaving,
  editingId,
  onSubmit,
  onCancel,
}) {
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

      <label className="ui-field">
        <span className="ui-field__label">Tipo</span>
        <select className="ui-input" {...register('anotTip', anotacionRules.anotTip)}>
          <option value="Positiva">Positiva</option>
          <option value="Negativa">Negativa</option>
        </select>
        {errors.anotTip ? <span className="ui-field__error">{errors.anotTip.message}</span> : null}
      </label>

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

      <div className="anotacion-form__two-cols">
        <Input
          label="RUT funcionario"
          type="number"
          placeholder="12345678"
          error={errors.funcionarioUsuRut?.message}
          {...register('funcionarioUsuRut', anotacionRules.funcionarioUsuRut)}
        />

        <Input
          label="ID hoja de vida"
          type="number"
          placeholder="1"
          error={errors.idHojaVida?.message}
          {...register('idHojaVida', anotacionRules.idHojaVida)}
        />
      </div>

      <Button type="submit" disabled={isSaving}>
        {isSaving ? 'Guardando...' : editingId ? 'Actualizar anotación' : 'Crear anotación'}
      </Button>
    </form>
  )
}
