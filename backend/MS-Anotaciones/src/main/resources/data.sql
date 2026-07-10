-- Seed de anotaciones para MS-Anotaciones.
-- Mapeo id_hoja_vida <-> estudiante: HojaVidaEstudiante usa IDENTITY y el
-- DatabaseSeeder de MS-HojaDeVida crea las 40 hojas en orden i=1..40 para los
-- estudiantes 26000001..26000040. Por lo tanto id_hoja_vida = N corresponde
-- al estudiante 26000000+N (rango valido: 1 a 40).
-- Solo 20 de los 40 estudiantes (hojas de vida) tienen anotaciones aqui:
-- 2, 3, 5, 7, 8, 10, 11, 13, 15, 16, 18, 20, 22, 23, 25, 27, 29, 31, 34, 36.
-- anot_gravedad solo aplica a anotaciones 'Negativa' (Leve/Grave/Muy Grave);
-- para 'Positiva' se deja NULL. funcionario_usu_rut se reparte entre
-- docentes e inspectores sembrados por MS-Autenticacion/DatabaseSeeder.
INSERT INTO anotaciones (anot_tip, anot_gravedad, anot_des, anot_fec, funcionario_usu_rut, id_hoja_vida)
VALUES
-- id_hoja_vida = 2 (1 anotacion)
('Positiva', NULL, 'Mostró excelente disposición para colaborar con sus compañeros durante el trabajo en equipo de Ciencias.', '2026-03-15', 15234123, 2),

-- id_hoja_vida = 3 (2 anotaciones)
('Positiva', NULL, 'Se destacó por su responsabilidad al traer siempre los materiales solicitados para la asignatura.', '2026-03-20', 17823456, 3),
('Negativa', 'Leve', 'Registró tres atrasos consecutivos a primera hora durante la semana.', '2026-04-05', 16111222, 3),

-- id_hoja_vida = 5 (3 anotaciones)
('Positiva', NULL, 'Lideró de forma proactiva la organización del aseo de la sala de clases.', '2026-03-10', 16543987, 5),
('Negativa', 'Grave', 'Utilizó un tono irrespetuoso hacia la docente durante la clase de Lenguaje.', '2026-05-02', 18765432, 5),
('Positiva', NULL, 'Mostró notable mejora en su comportamiento tras compromiso firmado con el profesor jefe.', '2026-08-14', 17123456, 5),

-- id_hoja_vida = 7 (4 anotaciones)
('Negativa', 'Leve', 'No presentó el cuaderno ni los materiales solicitados para la clase de Matemática.', '2026-03-25', 20123456, 7),
('Positiva', NULL, 'Participó activamente en la actividad de izamiento de bandera representando a su curso.', '2026-04-18', 13456789, 7),
('Negativa', 'Muy Grave', 'Protagonizó una pelea con un compañero durante el recreo en el patio central.', '2026-06-09', 19444555, 7),
('Positiva', NULL, 'Ayudó a un compañero con dificultades de aprendizaje durante la clase de Historia.', '2026-09-22', 15987654, 7),

-- id_hoja_vida = 8 (2 anotaciones)
('Positiva', NULL, 'Demostró puntualidad ejemplar durante todo el mes en la asignatura de Inglés.', '2026-03-30', 18234567, 8),
('Negativa', 'Leve', 'Utilizó el celular durante la clase pese a la advertencia previa.', '2026-05-15', 20555666, 8),

-- id_hoja_vida = 10 (1 anotacion)
('Negativa', 'Grave', 'Fue sorprendido copiando en la evaluación de Ciencias Naturales.', '2026-04-11', 19987654, 10),

-- id_hoja_vida = 11 (3 anotaciones)
('Positiva', NULL, 'Representó al colegio en el encuentro deportivo intercolegial con destacada actitud.', '2026-03-18', 21234567, 11),
('Negativa', 'Leve', 'Ingresó atrasado a clases en reiteradas ocasiones durante la semana.', '2026-06-20', 14678901, 11),
('Positiva', NULL, 'Colaboró activamente en la mediación de un conflicto entre compañeros de curso.', '2026-10-05', 16890123, 11),

-- id_hoja_vida = 13 (2 anotaciones)
('Negativa', 'Muy Grave', 'Causó daño intencional al mobiliario de la sala de clases.', '2026-05-28', 16111222, 13),
('Positiva', NULL, 'Mostró una actitud respetuosa y colaborativa con el personal auxiliar del establecimiento.', '2026-08-02', 17222333, 13),

-- id_hoja_vida = 15 (4 anotaciones)
('Positiva', NULL, 'Presentó iniciativa destacada en el desarrollo del proyecto de curso sobre medio ambiente.', '2026-03-22', 15234123, 15),
('Negativa', 'Leve', 'No utilizó el uniforme escolar completo durante la jornada.', '2026-04-30', 18333444, 15),
('Negativa', 'Grave', 'Empleó lenguaje inapropiado hacia un compañero durante la clase de Educación Física.', '2026-07-14', 19345678, 15),
('Positiva', NULL, 'Entregó con anticipación su trabajo de investigación demostrando gran compromiso.', '2026-11-03', 14256789, 15),

-- id_hoja_vida = 16 (1 anotacion)
('Positiva', NULL, 'Se mostró motivado y colaborativo durante la actividad deportiva organizada por el curso.', '2026-05-09', 18765432, 16),

-- id_hoja_vida = 18 (3 anotaciones)
('Negativa', 'Leve', 'Fue registrado utilizando redes sociales durante el desarrollo de la clase.', '2026-03-27', 20123456, 18),
('Positiva', NULL, 'Apoyó a sus compañeros en la organización de la salida pedagógica al museo.', '2026-06-16', 17123456, 18),
('Negativa', 'Grave', 'Mostró una actitud desafiante frente a la indicación del inspector de patio.', '2026-09-09', 19444555, 18),

-- id_hoja_vida = 20 (2 anotaciones)
('Positiva', NULL, 'Destacó por su liderazgo positivo durante el trabajo colaborativo de Historia.', '2026-04-14', 13456789, 20),
('Negativa', 'Leve', 'Presentó ausencia injustificada durante la jornada de la tarde.', '2026-07-21', 16111222, 20),

-- id_hoja_vida = 22 (1 anotacion)
('Positiva', NULL, 'Mostró gran responsabilidad al cumplir puntualmente con todas sus tareas de la semana.', '2026-08-25', 15987654, 22),

-- id_hoja_vida = 23 (4 anotaciones)
('Negativa', 'Grave', 'Interrumpió constantemente el desarrollo de la clase pese a los llamados de atención.', '2026-03-12', 18234567, 23),
('Positiva', NULL, 'Colaboró de manera destacada con el profesor jefe en la organización del consejo de curso.', '2026-05-06', 21234567, 23),
('Negativa', 'Muy Grave', 'Se vio involucrado en una agresión física hacia un compañero durante el recreo.', '2026-09-30', 20555666, 23),
('Positiva', NULL, 'Mostró una notable superación académica tras un período de apoyo pedagógico.', '2026-11-10', 16890123, 23),

-- id_hoja_vida = 25 (2 anotaciones)
('Positiva', NULL, 'Participó con entusiasmo en la actividad extracurricular de teatro del colegio.', '2026-04-02', 14678901, 25),
('Negativa', 'Leve', 'No trajo los materiales necesarios para la clase de Artes Visuales.', '2026-06-25', 17222333, 25),

-- id_hoja_vida = 27 (3 anotaciones)
('Negativa', 'Leve', 'Acumuló atrasos reiterados a la salida de recreo durante la semana.', '2026-03-19', 19987654, 27),
('Positiva', NULL, 'Mostró una actitud respetuosa y colaborativa durante la actividad de convivencia escolar.', '2026-07-08', 18333444, 27),
('Negativa', 'Grave', 'Faltó el respeto a un docente al responder de manera inapropiada en clase.', '2026-10-15', 19345678, 27),

-- id_hoja_vida = 29 (1 anotacion)
('Positiva', NULL, 'Se destacó por su compromiso y buena disposición en la actividad solidaria del curso.', '2026-05-20', 15234123, 29),

-- id_hoja_vida = 31 (2 anotaciones)
('Negativa', 'Muy Grave', 'Protagonizó un incidente de agresión verbal grave hacia un compañero de curso.', '2026-04-23', 16111222, 31),
('Positiva', NULL, 'Mostró una mejora sostenida en su comportamiento durante el segundo semestre.', '2026-08-19', 17823456, 31),

-- id_hoja_vida = 34 (3 anotaciones)
('Positiva', NULL, 'Colaboró activamente en la campaña de reciclaje organizada por el curso.', '2026-03-24', 18765432, 34),
('Negativa', 'Leve', 'Salió de la sala de clases sin autorización durante el horario de Matemática.', '2026-06-04', 20123456, 34),
('Negativa', 'Grave', 'Incumplió reiteradamente las normas de convivencia establecidas por el curso.', '2026-10-28', 14256789, 34),

-- id_hoja_vida = 36 (2 anotaciones)
('Positiva', NULL, 'Mostró una destacada actitud de respeto y compañerismo durante la salida a terreno.', '2026-05-13', 13456789, 36),
('Negativa', 'Leve', 'No entregó a tiempo la tarea correspondiente a la asignatura de Lenguaje.', '2026-09-17', 19987654, 36);
