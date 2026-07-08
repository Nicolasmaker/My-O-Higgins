import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'

function fechaCL(value) {
  if (!value) return '—'
  return new Date(`${value}T00:00:00`).toLocaleDateString('es-CL')
}

function fechaHoraCL(value) {
  if (!value) return '—'
  return new Date(value).toLocaleDateString('es-CL')
}

// Genera y descarga el PDF completo de una hoja de vida: antecedentes académicos, de
// apoderado y médicos, anotaciones, documentos adjuntos (solo su nombre, no el contenido)
// e historial de matrículas. Todo el dato ya viene cargado client-side en HojaDeVida.jsx,
// no se hace ninguna llamada nueva al backend acá.
export function generarHojaVidaPdf({
  hoja,
  nombreEstudiante,
  rutFormateado,
  anotaciones = [],
  documentos = [],
  matriculas = [],
  academicos = [],
  apoderados = [],
  medicos = [],
}) {
  const doc = new jsPDF()
  const margenIzq = 14
  let y = 18

  doc.setFontSize(16)
  doc.text('Hoja de Vida del Estudiante', margenIzq, y)
  y += 8

  doc.setFontSize(11)
  doc.text(`Estudiante: ${nombreEstudiante || '—'}`, margenIzq, y)
  y += 6
  doc.text(`RUT: ${rutFormateado || '—'}`, margenIzq, y)
  y += 6
  doc.text(`Hoja de vida #${hoja.idHojaVida} · Estado: ${hoja.estado || '—'}`, margenIzq, y)
  y += 6
  doc.text(`Emitido: ${fechaHoraCL(new Date().toISOString())}`, margenIzq, y)
  y += 10

  const seccion = (titulo) => {
    doc.setFontSize(13)
    doc.text(titulo, margenIzq, y)
    y += 2
  }

  const despuesDeTabla = () => {
    y = (doc.lastAutoTable?.finalY ?? y) + 10
  }

  // ── Antecedentes académicos ──
  seccion('Antecedentes académicos')
  if (academicos.length === 0) {
    doc.setFontSize(10)
    doc.text('Sin antecedentes académicos.', margenIzq, y + 6)
    y += 14
  } else {
    autoTable(doc, {
      startY: y + 4,
      margin: { left: margenIzq },
      head: [['Año escolar', 'Promedio', 'Aprobado']],
      body: academicos.map((a) => [
        a.anioEscolar,
        a.promedioGeneralActual?.toFixed?.(1) ?? a.promedioGeneralActual,
        a.situacionFinalAprobacion === 'S' ? 'Sí' : 'No',
      ]),
      styles: { fontSize: 9 },
      headStyles: { fillColor: [107, 35, 35] },
    })
    despuesDeTabla()
  }

  // ── Antecedentes de apoderado ──
  seccion('Antecedentes de apoderado')
  if (apoderados.length === 0) {
    doc.setFontSize(10)
    doc.text('Sin antecedentes de apoderado.', margenIzq, y + 6)
    y += 14
  } else {
    autoTable(doc, {
      startY: y + 4,
      margin: { left: margenIzq },
      head: [['Nombre', 'Profesión', 'Teléfono', 'Lugar de trabajo', 'Disponibilidad']],
      body: apoderados.map((a) => [
        a.nombre,
        a.profesion,
        a.telefono,
        a.lugarTrabajo,
        a.disponibilidadHoraria === 'S' ? 'Sí' : 'No',
      ]),
      styles: { fontSize: 9 },
      headStyles: { fillColor: [107, 35, 35] },
    })
    despuesDeTabla()
  }

  // ── Antecedentes médicos ──
  seccion('Antecedentes médicos')
  if (medicos.length === 0) {
    doc.setFontSize(10)
    doc.text('Sin antecedentes médicos.', margenIzq, y + 6)
    y += 14
  } else {
    autoTable(doc, {
      startY: y + 4,
      margin: { left: margenIzq },
      head: [['Tipo de sangre', 'Alergias', 'Medicamentos', 'Condiciones médicas', 'Observaciones']],
      body: medicos.map((a) => [a.tipoSangre, a.alergias, a.medicamentos, a.condicionesMedicas, a.observaciones || '—']),
      styles: { fontSize: 9 },
      headStyles: { fillColor: [107, 35, 35] },
    })
    despuesDeTabla()
  }

  // ── Anotaciones ──
  if (y > 260) { doc.addPage(); y = 18 }
  seccion('Anotaciones')
  if (anotaciones.length === 0) {
    doc.setFontSize(10)
    doc.text('Sin anotaciones registradas.', margenIzq, y + 6)
    y += 14
  } else {
    autoTable(doc, {
      startY: y + 4,
      margin: { left: margenIzq },
      head: [['Fecha', 'Tipo', 'Descripción']],
      body: anotaciones.map((a) => [fechaCL(a.anotFec), a.anotTip, a.anotDes]),
      styles: { fontSize: 9 },
      headStyles: { fillColor: [107, 35, 35] },
      columnStyles: { 2: { cellWidth: 110 } },
    })
    despuesDeTabla()
  }

  // ── Historial de matrículas ──
  if (y > 260) { doc.addPage(); y = 18 }
  seccion('Historial de matrículas')
  if (matriculas.length === 0) {
    doc.setFontSize(10)
    doc.text('Sin matrículas registradas.', margenIzq, y + 6)
    y += 14
  } else {
    autoTable(doc, {
      startY: y + 4,
      margin: { left: margenIzq },
      head: [['Año', 'Estado', 'Tipo alumno', 'Fecha']],
      body: matriculas.map((m) => [m.matriculaAnioAcademico, m.matriculaEstado, m.tipoAlumno, fechaCL(m.matriculaFecha)]),
      styles: { fontSize: 9 },
      headStyles: { fillColor: [107, 35, 35] },
    })
    despuesDeTabla()
  }

  // ── Documentos oficiales (solo listado, no se embebe el contenido) ──
  if (y > 260) { doc.addPage(); y = 18 }
  seccion('Documentos oficiales adjuntos')
  if (documentos.length === 0) {
    doc.setFontSize(10)
    doc.text('Sin documentos adjuntos.', margenIzq, y + 6)
  } else {
    autoTable(doc, {
      startY: y + 4,
      margin: { left: margenIzq },
      head: [['Nombre', 'Fecha de subida']],
      body: documentos.map((d) => [d.nombreArchivo, fechaCL(d.fechaSubida)]),
      styles: { fontSize: 9 },
      headStyles: { fillColor: [107, 35, 35] },
    })
  }

  const nombreArchivo = `hoja-de-vida-${hoja.estudianteUsuRut}.pdf`
  doc.save(nombreArchivo)
}
