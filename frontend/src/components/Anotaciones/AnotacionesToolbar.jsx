import Button from '../UI/Button/Button'
import './AnotacionesToolbar.css'

export default function AnotacionesToolbar({ total, modeLabel, canSeeLabel, filterHojaVida, setFilterHojaVida, onSearch, onReset }) {
  return (
    <section className="anotaciones-toolbar">
      <form className="anotaciones-toolbar__filter" onSubmit={onSearch}>
        <label>
          Filtrar por hoja de vida
          <input
            type="number"
            min="1"
            value={filterHojaVida}
            onChange={(event) => setFilterHojaVida(event.target.value)}
            placeholder="ID hoja de vida"
          />
        </label>
        <Button type="submit">Buscar</Button>
        <Button type="button" variant="ghost" onClick={onReset}>
          Ver todo
        </Button>
      </form>

      <div className="anotaciones-toolbar__stats">
        <article>
          <span>Total</span>
          <strong>{total}</strong>
        </article>
        <article>
          <span>Modo</span>
          <strong>{modeLabel}</strong>
        </article>
        <article>
          <span>Permiso visible</span>
          <strong>{canSeeLabel}</strong>
        </article>
      </div>
    </section>
  )
}
