// Formatea un RUT con puntos de miles y DV opcional: (12345678, '9') -> "12.345.678-9".
// Sin DV (aún no resuelto por el backend, o el usuario no existe) devuelve solo el número con puntos.
export function formatRut(rut, dv) {
  if (rut === null || rut === undefined || rut === '') return null
  const conPuntos = String(rut).replace(/\B(?=(\d{3})+(?!\d))/g, '.')
  return dv ? `${conPuntos}-${dv}` : conPuntos
}
