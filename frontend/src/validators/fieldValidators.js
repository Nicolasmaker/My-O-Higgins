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
  idHojaVida: {
    required: 'El ID de hoja de vida es obligatorio',
    valueAsNumber: true,
    min: {
      value: 1,
      message: 'El ID debe ser mayor a 0',
    },
  },
}
