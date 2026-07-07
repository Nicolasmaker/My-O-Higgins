import Button from '../UI/Button'
import './AnotacionesToolbar.css'

export default function AnotacionesToolbar({
  filterRut,
  setFilterRut,
  onSearch,
  onReset,
  onRefresh,
  filterTipo,
  setFilterTipo,
  filterCurso,
  setFilterCurso,
  filterGravedad,
  setFilterGravedad,
}) {
  return (
    <section className="anotaciones-toolbar">
      <form className="anotaciones-toolbar__filter" onSubmit={onSearch}>
        <label>
          Tipo
          <select value={filterTipo} onChange={(event) => setFilterTipo(event.target.value)}>
            <option value="todas">Todas</option>
            <option value="positiva">Positivas</option>
            <option value="negativa">Negativas</option>
          </select>
        </label>
        <label>
          Gravedad
          <select value={filterGravedad} onChange={(event) => setFilterGravedad(event.target.value)}>
            <option value="todas">Todas</option>
            <option value="Leve">Leve</option>
            <option value="Grave">Grave</option>
            <option value="Muy Grave">Muy Grave</option>
          </select>
        </label>
        <label>
          Curso
          <input
            type="text"
            className="anotaciones-toolbar__input--curso"
            value={filterCurso}
            onChange={(event) => setFilterCurso(event.target.value)}
            placeholder="8°B"
          />
        </label>
        <label>
          Estudiante (RUT o nombre)
          <input
            type="text"
            className="anotaciones-toolbar__input--estudiante"
            value={filterRut}
            onChange={(event) => setFilterRut(event.target.value)}
            placeholder="Juan Pérez o 12345678-5"
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
