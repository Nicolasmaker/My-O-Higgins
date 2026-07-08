INSERT INTO MURAL_DIGITAL (
  mur_dig_titulo,
  mur_dig_contenido,
  mur_dig_fec_pub,
  FUNCIONARIO_usu_rut
) VALUES
('Bienvenida segundo semestre', 'Damos la bienvenida a toda la comunidad escolar al segundo semestre 2026. Revisar horarios actualizados en secretaría.', '2026-07-10', 10111222),
('Campaña de vacunación', 'El día 30 de julio se realizará la campaña de vacunación escolar en la enfermería del establecimiento.', '2026-07-12', 10111222),
('Taller de Robótica Educativa', 'Inscripciones abiertas para el Taller de Robótica Educativa, todos los niveles de enseñanza básica y media. Cupos limitados, inscríbete en UTP.', '2026-07-14', 10111222),
('Taller de Teatro Escolar', 'Nuevo taller extraprogramático de teatro los días miércoles en el gimnasio techado. Abierto a estudiantes de 5° básico a 4° medio.', '2026-07-16', 10111222),
('Semana de la Chilenidad — Fiestas Patrias', 'Del 14 al 18 de septiembre celebramos la Semana de la Chilenidad: cueca, ramadas internas, juegos típicos y gastronomía tradicional. ¡Prepara tu traje típico!', '2026-08-20', 10111222),
('Feria Científica y Tecnológica', 'Cada curso presentará un proyecto científico. Se premiarán las 3 mejores iniciativas de innovación. Fecha: primera semana de octubre en el patio central.', '2026-08-25', 10111222),
('Taller de Ajedrez', 'Club de ajedrez abierto todos los martes en horario de almuerzo, biblioteca CRA. No se necesita experiencia previa.', '2026-07-18', 10111222),
('Charla de Orientación Vocacional', 'Estudiantes de 3° y 4° medio: charla informativa sobre carreras técnicas y universitarias, con invitados de distintas casas de estudio.', '2026-08-05', 10111222),
('Campeonato Deportivo Interescolar', 'Se viene el campeonato de fútbol y vóleibol entre cursos. Inscripción de equipos con el profesor de Educación Física hasta el 30 de julio.', '2026-07-22', 10111222),
('Taller de Huerto Escolar', 'Programa de huerto escolar sustentable: todos los cursos de enseñanza básica participan una vez al mes en el cuidado del huerto del colegio.', '2026-07-25', 10111222);

INSERT INTO CALENDARIO_ESTUDIANTIL (
  cal_est_tit_eve,
  cal_est_tip_eve,
  cal_est_fec_ini,
  cal_est_fec_fin,
  MURAL_DIGITAL_id_mur_dig,
  ASIGNATURA_id_asi,
  cal_est_des_eve
) VALUES
('Inicio segundo semestre', 'Institucional', '2026-07-15', '2026-07-15', NULL, 1, 'Inicio oficial del segundo semestre escolar.'),
('Prueba de Matemáticas 4 Medio', 'Académico', '2026-07-20', '2026-07-20', NULL, 1, 'Evaluación escrita de contenidos de álgebra.'),
('Reunión de apoderados 1A', 'Reunión', '2026-07-24', '2026-07-24', NULL, 2, 'Encuentro informativo con apoderados del curso 1A.'),
('Acto cívico institucional', 'Actividad', '2026-07-28', '2026-07-28', NULL, 3, 'Ceremonia institucional en gimnasio techado.'),
('Taller de Robótica Educativa', 'Actividad', '2026-07-31', '2026-07-31', NULL, 1, 'Primera sesión del taller de robótica en el laboratorio de ciencias.'),
('Campeonato Deportivo Interescolar', 'Actividad', '2026-08-08', '2026-08-08', NULL, 1, 'Jornada inaugural del campeonato de fútbol y vóleibol entre cursos.'),
('Charla de Orientación Vocacional', 'Institucional', '2026-08-12', '2026-08-12', NULL, 1, 'Charla para 3° y 4° medio con invitados de universidades e institutos.'),
('Semana de la Chilenidad — Fiestas Patrias', 'Actividad', '2026-09-14', '2026-09-18', NULL, 1, 'Cueca, ramadas internas, juegos típicos y gastronomía tradicional en todo el colegio.'),
('Feria Científica y Tecnológica', 'Actividad', '2026-10-05', '2026-10-05', NULL, 1, 'Exposición de proyectos científicos de todos los cursos en el patio central.');
