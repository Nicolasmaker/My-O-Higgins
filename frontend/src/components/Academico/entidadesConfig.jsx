// =============================================================
// CONFIGURACIÓN DE ENTIDADES ACADÉMICAS — entidadesConfig.jsx
// =============================================================
// Define las 7 entidades del MS-GestionAcademica de forma
// declarativa: columnas de tabla, campos de formulario y armado
// de payloads. La página y el modal genérico se alimentan de acá.
//
// Ojo con el backend: los nombres de campo de los REQUEST no
// siempre coinciden con los de la ENTIDAD que devuelve el GET
// (ej: request `asiNom` vs entidad `asiNombre`). Por eso cada
// campo declara `getValue(item)` para precargar la edición.
// =============================================================
import { Badge } from 'react-bootstrap'
import { limpiarRut } from '../../validators/fieldValidators'
import {
  getCursos, crearCurso, actualizarCurso, eliminarCurso,
  getAsignaturas, crearAsignatura, actualizarAsignatura, eliminarAsignatura,
  getNiveles, crearNivel, actualizarNivel, eliminarNivel,
  getSalas, crearSala, actualizarSala, eliminarSala,
  getEvaluaciones, crearEvaluacion, actualizarEvaluacion, eliminarEvaluacion,
  getBitacorasAsignatura, crearBitacoraAsignatura, actualizarBitacoraAsignatura, eliminarBitacoraAsignatura,
  getImpartir, crearImpartir, eliminarImpartir,
  getNotas, registrarNota, actualizarNota, eliminarNota,
} from '../../services/academicoService'

function fecha(value) {
  if (!value) return '—'
  return new Date(`${value}T00:00:00`).toLocaleDateString('es-CL')
}

export const ENTIDADES = {
  curso: {
    titulo: 'Cursos',
    singular: 'curso',
    nuevoLabel: '+ Nuevo curso',
    idKey: 'idCur',
    api: { list: getCursos, crear: crearCurso, actualizar: actualizarCurso, eliminar: eliminarCurso },
    columnas: [
      { label: '#', render: (i) => i.idCur },
      { label: 'Sección', render: (i) => i.curLetraSeccion },
      { label: 'Año escolar', render: (i) => i.curAnioEscolar },
      { label: 'Nivel', render: (i) => i.nivel?.nivNum ?? '—' },
      { label: 'Sala', render: (i) => i.sala?.salLetra ?? '—' },
    ],
    campos: [
      { name: 'curLetraSec', label: 'Letra sección', maxLength: 1, width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.curLetraSeccion ?? '' },
      { name: 'curAnioEscolar', label: 'Año escolar', type: 'number', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.curAnioEscolar ?? '', soloCrear: true },
      { name: 'idNivel', label: 'Nivel', type: 'entity-select', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.nivel?.idNiv ?? '', soloCrear: true, loadOptions: () => getNiveles().then((r) => r.data), optionValue: (o) => o.idNiv, optionLabel: (o) => `Nivel ${o.nivNum}` },
      { name: 'idSala', label: 'Sala', type: 'entity-select', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.sala?.idSal ?? '', loadOptions: () => getSalas().then((r) => r.data), optionValue: (o) => o.idSal, optionLabel: (o) => `Sala ${o.salLetra} (cap. ${o.salaCapacidad})` },
    ],
    defaults: { curLetraSec: '', curAnioEscolar: new Date().getFullYear(), idNivel: '', idSala: '' },
    payload: (d, editing) =>
      editing
        ? { curLetraSec: d.curLetraSec, idSala: Number(d.idSala) }
        : { curLetraSec: d.curLetraSec, curAnioEscolar: Number(d.curAnioEscolar), idNivel: Number(d.idNivel), idSala: Number(d.idSala) },
  },

  asignatura: {
    titulo: 'Asignaturas',
    singular: 'asignatura',
    nuevoLabel: '+ Nueva asignatura',
    idKey: 'idAsi',
    api: { list: getAsignaturas, crear: crearAsignatura, actualizar: actualizarAsignatura, eliminar: eliminarAsignatura },
    columnas: [
      { label: '#', render: (i) => i.idAsi },
      { label: 'Nombre', render: (i) => i.asiNombre },
      { label: 'Descripción', render: (i) => i.asiDescripcion },
    ],
    campos: [
      { name: 'asiNom', label: 'Nombre', maxLength: 30, width: 12, rules: { required: 'Obligatorio' }, getValue: (i) => i.asiNombre ?? '' },
      { name: 'asiDes', label: 'Descripción', type: 'textarea', maxLength: 200, width: 12, rules: { required: 'Obligatorio' }, getValue: (i) => i.asiDescripcion ?? '' },
    ],
    defaults: { asiNom: '', asiDes: '' },
    payload: (d) => ({ asiNom: d.asiNom, asiDes: d.asiDes }),
  },

  nivel: {
    titulo: 'Niveles',
    singular: 'nivel',
    nuevoLabel: '+ Nuevo nivel',
    idKey: 'idNiv',
    api: { list: getNiveles, crear: crearNivel, actualizar: actualizarNivel, eliminar: eliminarNivel },
    columnas: [
      { label: '#', render: (i) => i.idNiv },
      { label: 'Número de nivel', render: (i) => i.nivNum },
    ],
    campos: [
      { name: 'nivNum', label: 'Número de nivel', type: 'number', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.nivNum ?? '' },
    ],
    defaults: { nivNum: '' },
    payload: (d) => ({ nivNum: Number(d.nivNum) }),
  },

  sala: {
    titulo: 'Salas',
    singular: 'sala',
    nuevoLabel: '+ Nueva sala',
    idKey: 'idSal',
    api: { list: getSalas, crear: crearSala, actualizar: actualizarSala, eliminar: eliminarSala },
    columnas: [
      { label: '#', render: (i) => i.idSal },
      { label: 'Sala', render: (i) => i.salLetra },
      { label: 'Capacidad', render: (i) => i.salaCapacidad },
    ],
    campos: [
      { name: 'salLetra', label: 'Identificador sala', maxLength: 5, width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.salLetra ?? '' },
      { name: 'salaCapacidad', label: 'Capacidad', type: 'number', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.salaCapacidad ?? '' },
    ],
    defaults: { salLetra: '', salaCapacidad: '' },
    payload: (d) => ({ salLetra: d.salLetra, salaCapacidad: Number(d.salaCapacidad) }),
  },

  evaluacion: {
    titulo: 'Evaluaciones',
    singular: 'evaluación',
    nuevoLabel: '+ Nueva evaluación',
    idKey: 'idEva',
    api: { list: getEvaluaciones, crear: crearEvaluacion, actualizar: actualizarEvaluacion, eliminar: eliminarEvaluacion },
    columnas: [
      { label: '#', render: (i) => i.idEva },
      { label: 'Nombre', render: (i) => i.evaNom },
      { label: 'Fecha', render: (i) => fecha(i.evaFecha) },
      { label: 'Período', render: (i) => i.evaPeriodoAcad },
      { label: 'Tipo', render: (i) => <Badge bg="secondary">{i.evaTipo}</Badge> },
      { label: 'Asignatura', render: (i) => i.asignatura?.asiNombre ?? '—' },
      { label: 'Curso', render: (i) => (i.curso ? `${i.curso.nivel?.nivNum ?? '—'}°${i.curso.curLetraSeccion}` : '—') },
      { label: 'Docente', render: (i) => i.docenteUsuRut },
    ],
    campos: [
      { name: 'evaNom', label: 'Nombre', maxLength: 100, width: 12, rules: { required: 'Obligatorio' }, getValue: (i) => i.evaNom ?? '' },
      { name: 'evaFec', label: 'Fecha', type: 'date', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.evaFecha ?? '' },
      { name: 'evaPerioAcad', label: 'Período académico', maxLength: 20, width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.evaPeriodoAcad ?? '' },
      { name: 'evaTip', label: 'Tipo', type: 'select', options: ['Prueba', 'Control', 'Trabajo', 'Examen'], width: 6, rules: { required: true }, getValue: (i) => i.evaTipo ?? 'Prueba' },
      { name: 'docenteUsuRut', label: 'RUT docente', type: 'rut', width: 6, getValue: (i) => i.docenteUsuRut ?? '' },
      { name: 'idAsignatura', label: 'Asignatura', type: 'entity-select', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.asignatura?.idAsi ?? '', soloCrear: true, loadOptions: () => getAsignaturas().then((r) => r.data), optionValue: (o) => o.idAsi, optionLabel: (o) => o.asiNombre },
      { name: 'idCurso', label: 'Curso', type: 'entity-select', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.curso?.idCur ?? '', loadOptions: () => getCursos().then((r) => r.data), optionValue: (o) => o.idCur, optionLabel: (o) => `${o.nivel?.nivNum ?? '—'}°${o.curLetraSeccion} (${o.curAnioEscolar})` },
    ],
    defaults: { evaNom: '', evaFec: '', evaPerioAcad: '', evaTip: 'Prueba', docenteUsuRut: '', idAsignatura: '', idCurso: '' },
    payload: (d, editing) => {
      const base = {
        evaNom: d.evaNom,
        evaFec: d.evaFec,
        evaPerioAcad: d.evaPerioAcad,
        evaTip: d.evaTip,
        docenteUsuRut: limpiarRut(d.docenteUsuRut),
        idCurso: Number(d.idCurso),
      }
      return editing ? base : { ...base, idAsignatura: Number(d.idAsignatura) }
    },
  },

  bitacora: {
    titulo: 'Bitácoras de asignatura',
    singular: 'bitácora',
    nuevoLabel: '+ Nueva bitácora',
    idKey: 'idBitAsi',
    api: { list: getBitacorasAsignatura, crear: crearBitacoraAsignatura, actualizar: actualizarBitacoraAsignatura, eliminar: eliminarBitacoraAsignatura },
    columnas: [
      { label: '#', render: (i) => i.idBitAsi },
      { label: 'Fecha clase', render: (i) => fecha(i.bitAsiFechaClase) },
      { label: 'Asignatura', render: (i) => i.asignatura?.asiNombre ?? '—' },
      { label: 'Actividades', render: (i) => i.bitAsiActividades },
      { label: 'Contenidos', render: (i) => i.bitAsiContenidos },
    ],
    campos: [
      { name: 'bitAsiFecClase', label: 'Fecha de la clase', type: 'date', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.bitAsiFechaClase ?? '' },
      { name: 'idAsignatura', label: 'Asignatura', type: 'entity-select', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.asignatura?.idAsi ?? '', soloCrear: true, loadOptions: () => getAsignaturas().then((r) => r.data), optionValue: (o) => o.idAsi, optionLabel: (o) => o.asiNombre },
      { name: 'bitAsiActividades', label: 'Actividades', type: 'textarea', maxLength: 1000, width: 12, rules: { required: 'Obligatorio' }, getValue: (i) => i.bitAsiActividades ?? '' },
      { name: 'bitAsiContenidos', label: 'Contenidos', type: 'textarea', maxLength: 1000, width: 12, rules: { required: 'Obligatorio' }, getValue: (i) => i.bitAsiContenidos ?? '' },
      { name: 'bitAsiObs', label: 'Observaciones', type: 'textarea', maxLength: 1000, width: 12, rules: { required: 'Obligatorio' }, getValue: (i) => i.bitAsiObs ?? '' },
      { name: 'bitAsiObjApren', label: 'Objetivos de aprendizaje', type: 'textarea', maxLength: 500, width: 12, getValue: (i) => i.bitAsiObjApren ?? '' },
    ],
    defaults: { bitAsiFecClase: '', idAsignatura: '', bitAsiActividades: '', bitAsiContenidos: '', bitAsiObs: '', bitAsiObjApren: '' },
    payload: (d, editing) => {
      const base = {
        bitAsiFecClase: d.bitAsiFecClase,
        bitAsiActividades: d.bitAsiActividades,
        bitAsiContenidos: d.bitAsiContenidos,
        bitAsiObs: d.bitAsiObs,
        bitAsiObjApren: d.bitAsiObjApren || null,
      }
      return editing ? base : { ...base, idAsignatura: Number(d.idAsignatura) }
    },
  },

  impartir: {
    titulo: 'Asignaciones docentes',
    singular: 'asignación',
    nuevoLabel: '+ Nueva asignación',
    idKey: 'idImp',
    sinEditar: true, // el backend no tiene PUT /impartir/{id}, solo crear y eliminar
    api: { list: getImpartir, crear: crearImpartir, eliminar: eliminarImpartir },
    columnas: [
      { label: '#', render: (i) => i.idImp },
      { label: 'RUT docente', render: (i) => i.docenteUsuRut },
      { label: 'Asignatura', render: (i) => i.asignatura?.asiNombre ?? '—' },
      { label: 'Curso', render: (i) => (i.curso ? `${i.curso.nivel?.nivNum ?? '—'}°${i.curso.curLetraSeccion ?? ''}` : '—') },
    ],
    campos: [
      { name: 'docenteUsuRut', label: 'RUT docente', type: 'rut', width: 6, getValue: (i) => i.docenteUsuRut ?? '' },
      { name: 'idAsignatura', label: 'Asignatura', type: 'entity-select', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.asignatura?.idAsi ?? '', loadOptions: () => getAsignaturas().then((r) => r.data), optionValue: (o) => o.idAsi, optionLabel: (o) => o.asiNombre },
      { name: 'idCurso', label: 'Curso', type: 'entity-select', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.curso?.idCur ?? '', loadOptions: () => getCursos().then((r) => r.data), optionValue: (o) => o.idCur, optionLabel: (o) => `${o.nivel?.nivNum ?? '—'}°${o.curLetraSeccion} (${o.curAnioEscolar})` },
    ],
    defaults: { docenteUsuRut: '', idAsignatura: '', idCurso: '' },
    payload: (d) => ({
      docenteUsuRut: limpiarRut(d.docenteUsuRut),
      idAsignatura: Number(d.idAsignatura),
      idCurso: Number(d.idCurso),
    }),
  },

  notas: {
    titulo: 'Notas',
    singular: 'nota',
    nuevoLabel: '+ Nueva nota',
    idKey: 'idNot',
    api: { list: getNotas, crear: registrarNota, actualizar: actualizarNota, eliminar: eliminarNota },
    columnas: [
      { label: '#', render: (i) => i.idNot },
      {
        label: 'Calificación',
        render: (i) => (
          <Badge bg={i.notCalif >= 4 ? 'success' : 'danger'}>{Number(i.notCalif).toFixed(1)}</Badge>
        ),
      },
      { label: 'Estudiante', render: (i) => i.estudianteUsuRut },
      { label: 'Evaluación', render: (i) => i.evaluacion?.evaNom ?? '—' },
      { label: 'Registrada', render: (i) => fecha(i.notFechaRegistrada) },
    ],
    campos: [
      { name: 'notCalif', label: 'Calificación (1.0–7.0)', type: 'decimal', width: 6, rules: { required: 'Obligatorio', min: { value: 1, message: 'Mínimo 1.0' }, max: { value: 7, message: 'Máximo 7.0' } }, getValue: (i) => i.notCalif ?? '' },
      { name: 'notFechaReg', label: 'Fecha de registro', type: 'date', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.notFechaRegistrada ?? '' },
      { name: 'estudianteUsuRut', label: 'RUT estudiante', type: 'rut', width: 6, getValue: (i) => i.estudianteUsuRut ?? '', soloCrear: true },
      { name: 'idEvaluacion', label: 'Evaluación', type: 'entity-select', width: 6, rules: { required: 'Obligatorio' }, getValue: (i) => i.evaluacion?.idEva ?? '', soloCrear: true, loadOptions: () => getEvaluaciones().then((r) => r.data), optionValue: (o) => o.idEva, optionLabel: (o) => `${o.evaNom} — ${o.asignatura?.asiNombre ?? ''}` },
    ],
    defaults: { notCalif: '', notFechaReg: '', estudianteUsuRut: '', idEvaluacion: '' },
    payload: (d, editing) => {
      const base = { notCalif: Number(d.notCalif), notFechaReg: d.notFechaReg }
      return editing
        ? base
        : { ...base, estudianteUsuRut: limpiarRut(d.estudianteUsuRut), idEvaluacion: Number(d.idEvaluacion) }
    },
  },
}
