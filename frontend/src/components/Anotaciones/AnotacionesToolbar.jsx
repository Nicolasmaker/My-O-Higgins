import Button from '../UI/Button'
import './AnotacionesToolbar.css'

export default function AnotacionesToolbar({
  filterRut,
  setFilterRut,
  onSearch,
  onReset,
  onRefresh,
}) {
  return (
    <section className="anotaciones-toolbar">
      <form className="anotaciones-toolbar__filter" onSubmit={onSearch}>
        <label>
          Buscar por RUT del estudiante
          <input
            type="text"
            value={filterRut}
            onChange={(event) => setFilterRut(event.target.value)}
            placeholder="12345678-5"
          />
        </label>
        <Button type="submit">Buscar</Button>
        <Button type="button" variant="outline" onClick={onReset}>
          Limpiar filtros
        </Button>
        <Button type="button" variant="ghost" onClick={onRefresh}>
          Refrescar
        </Button>
      </form>
    </section>
  )
}
