// =============================================================
// VALIDACIÓN DE RUT CHILENO
// =============================================================
// El backend guarda el RUT como número SIN dígito verificador.
// En los formularios se acepta:
//   - "12345678"    → solo cuerpo (se valida largo 7-8 dígitos)
//   - "12345678-5"  → cuerpo + DV (se valida módulo 11)
// `limpiarRut` extrae el cuerpo numérico para enviar al backend.
// =============================================================

// Normaliza el RUT tecleado: quita puntos/espacios, sube a mayúscula (por la 'k') y
// reemplaza guiones "raros" (en-dash, em-dash, guion tipográfico — comunes al copiar/
// pegar desde Word) por el guion ASCII normal, para que el regex no falle en silencio.
function normalizarRut(valor) {
  return String(valor)
    .replace(/\./g, '')
    .replace(/\s/g, '')
    .replace(/[‐‑‒–—−]/g, '-')
    .toUpperCase()
}

// Calcula el dígito verificador por módulo 11
export function calcularDv(cuerpo) {
  let suma = 0
  let multiplo = 2
  for (let i = String(cuerpo).length - 1; i >= 0; i--) {
    suma += Number(String(cuerpo)[i]) * multiplo
    multiplo = multiplo === 7 ? 2 : multiplo + 1
  }
  const resto = 11 - (suma % 11)
  if (resto === 11) return '0'
  if (resto === 10) return 'K'
  return String(resto)
}

// Valida formato y (si viene) el DV. Acepta "12345678" o "12.345.678-5".
export function rutValido(valor) {
  const limpio = normalizarRut(valor)
  const match = limpio.match(/^(\d{7,8})(-([\dK]))?$/)
  if (!match) return false
  const [, cuerpo, , dv] = match
  if (dv === undefined) return true // sin DV: solo se valida el largo
  return calcularDv(cuerpo) === dv
}

// Igual que rutValido pero exige guión + DV (para el RUT de estudiante en
// Anotaciones: solo se busca la hoja de vida si el formato viene completo).
export function rutValidoConDv(valor) {
  const limpio = normalizarRut(valor)
  const match = limpio.match(/^(\d{7,8})-([\dK])$/)
  if (!match) return false
  const [, cuerpo, dv] = match
  return calcularDv(cuerpo) === dv
}

// Extrae el cuerpo numérico del RUT (lo que espera el backend)
export function limpiarRut(valor) {
  const limpio = normalizarRut(valor)
  return Number(limpio.split('-')[0])
}

// Extrae el dígito verificador del RUT (ej. "12345678-5" -> "5"). Devuelve null si el valor
// no trae DV — usado al crear cuentas nuevas (Estudiante/Apoderado), donde el backend guarda
// cuerpo y DV en columnas separadas (usuRut / usuDvRut).
export function extraerDv(valor) {
  const limpio = normalizarRut(valor)
  const partes = limpio.split('-')
  return partes.length > 1 ? partes[1] : null
}

// Regla lista para react-hook-form
export const rutRules = {
  required: 'El RUT es obligatorio',
  validate: (v) => rutValido(v) || 'RUT inválido (7-8 dígitos, DV opcional: 12345678-5)',
}

// Para Anotaciones: exige guión + DV (ej. "12345678-5"), no acepta el RUT sin DV.
export const rutConDvRules = {
  required: 'El RUT es obligatorio',
  validate: (v) => rutValidoConDv(v) || 'RUT inválido, debe incluir el guión y el DV (ej. 12345678-5)',
}

export const emailRules = {
  required: 'El email es obligatorio',
  pattern: {
    value: /^\S+@\S+\.\S+$/,
    message: 'Email inválido',
  },
}

export const passwordRules = {
  required: 'La contraseña es obligatoria',
  minLength: {
    value: 6,
    message: 'La contraseña debe tener al menos 6 caracteres',
  },
}

export const anotacionRules = {
  anotTip: {
    required: 'El tipo de anotación es obligatorio',
  },
  anotDes: {
    required: 'La descripción es obligatoria',
    minLength: {
      value: 5,
      message: 'La descripción debe tener al menos 5 caracteres',
    },
  },
  funcionarioUsuRut: {
    required: 'El RUT del funcionario es obligatorio',
    valueAsNumber: true,
    min: {
      value: 1,
      message: 'El RUT debe ser mayor a 0',
    },
  },
  rutEstudiante: rutConDvRules,
}
