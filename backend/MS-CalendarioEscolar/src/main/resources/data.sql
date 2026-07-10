-- ============================================================================
-- Seeder: MURAL_DIGITAL + CALENDARIO_ESTUDIANTIL
-- Fecha de referencia "hoy" del proyecto: 2026-07-08
--
-- Mapeo ASIGNATURA_id_asi (orden IDENTITY sembrado por MS-GestionAcademica):
--   1=Lenguaje y Comunicación, 2=Lengua y Literatura, 3=Matemática,
--   4=Historia Geografía y Ciencias Sociales, 5=Ciencias Naturales,
--   6=Idioma Extranjero Inglés, 7=Artes Visuales, 8=Música,
--   9=Educación Física y Salud, 10=Tecnología, 11=Orientación, 12=Física,
--   13=Química, 14=Biología, 15=Filosofía, 16=Ciencias para la Ciudadanía,
--   17=Educación Ciudadana, 18-25=asignaturas de Kínder.
--   Eventos sin asignatura concreta (actos cívicos, campeonatos, chilenidad,
--   feria general, reuniones de apoderados) usan ASIGNATURA_id_asi = NULL.
--
-- RUT de FUNCIONARIOS (sembrados por MS-Autenticacion), repartidos entre
-- directivos y docentes según el tema de cada publicación del mural:
--   Directivos: 10111222 (Mauricio Paredes, Director),
--               11222333 (Patricia Vergara, Jefe UTP),
--               13444555 (Claudia Navarro, Secretaria)
--   Docentes:   15234123 (Lenguaje), 17823456 (Matemáticas), 16543987 (Biología),
--               19345678 (Ed. Física), 14256789 (Historia), 18765432 (Música),
--               20123456 (Artes), 13456789 (Física), 15987654 (Química),
--               17123456 (Inglés)
--
-- Los MURAL_DIGITAL se insertan en el orden de abajo, por lo que su id
-- autogenerado corresponde a la posición 1..10 dentro del INSERT. Los eventos
-- de CALENDARIO_ESTUDIANTIL que corresponden a una publicación del mural usan
-- ese id en MURAL_DIGITAL_id_mur_dig; el resto queda en NULL.
--   1 = Taller de Robótica Educativa
--   2 = Charla de Orientación Vocacional
--   3 = Campeonato Deportivo Interescolar
--   4 = Semana de la Chilenidad — Fiestas Patrias
--   5 = Feria Científica y Tecnológica
--   6, 7, 8, 9, 10 = publicaciones informativas sin evento de calendario asociado
-- ============================================================================

INSERT INTO MURAL_DIGITAL (
  mur_dig_titulo,
  mur_dig_contenido,
  mur_dig_fec_pub,
  FUNCIONARIO_usu_rut
) VALUES
('Taller de Robótica Educativa', 'Inscripciones abiertas para el Taller de Robótica Educativa, todos los niveles de enseñanza básica y media. Cupos limitados, inscríbete en UTP.', '2026-03-16', 11222333),
('Charla de Orientación Vocacional', 'Estudiantes de 3° y 4° medio: charla informativa sobre carreras técnicas y universitarias, con invitados de distintas casas de estudio.', '2026-04-20', 11222333),
('Campeonato Deportivo Interescolar', 'Se viene el campeonato de fútbol y vóleibol entre cursos. Inscripción de equipos con el profesor de Educación Física hasta fines de mayo.', '2026-05-04', 19345678),
('Semana de la Chilenidad — Fiestas Patrias', 'Del 14 al 18 de septiembre celebramos la Semana de la Chilenidad: cueca, ramadas internas, juegos típicos y gastronomía tradicional. ¡Prepara tu traje típico!', '2026-08-20', 10111222),
('Feria Científica y Tecnológica', 'Cada curso presentará un proyecto científico. Se premiarán las 3 mejores iniciativas de innovación. Fecha: primera semana de octubre en el patio central.', '2026-09-01', 16543987),
('Bienvenida segundo semestre', 'Damos la bienvenida a toda la comunidad escolar al segundo semestre 2026. Revisar horarios actualizados en secretaría.', '2026-07-06', 13444555),
('Campaña de vacunación', 'El día 30 de julio se realizará la campaña de vacunación escolar en la enfermería del establecimiento.', '2026-07-10', 13444555),
('Taller de Teatro Escolar', 'Nuevo taller extraprogramático de teatro los días miércoles en el gimnasio techado. Abierto a estudiantes de 5° básico a 4° medio.', '2026-06-05', 18765432),
('Taller de Ajedrez', 'Club de ajedrez abierto todos los martes en horario de almuerzo, biblioteca CRA. No se necesita experiencia previa.', '2026-04-08', 14256789),
('Taller de Huerto Escolar', 'Programa de huerto escolar sustentable: todos los cursos de enseñanza básica participan una vez al mes en el cuidado del huerto del colegio.', '2026-03-23', 16543987);

INSERT INTO CALENDARIO_ESTUDIANTIL (
  cal_est_tit_eve,
  cal_est_tip_eve,
  cal_est_fec_ini,
  cal_est_fec_fin,
  MURAL_DIGITAL_id_mur_dig,
  ASIGNATURA_id_asi,
  cal_est_des_eve
) VALUES
('Inicio año escolar', 'Institucional', '2026-03-02', '2026-03-02', NULL, NULL, 'Ceremonia de inicio del año escolar 2026 con toda la comunidad educativa.'),
('Taller de Robótica Educativa', 'Actividad', '2026-03-23', '2026-03-23', 1, 10, 'Primera sesión del taller de robótica en el laboratorio de ciencias.'),
('Prueba de Historia 2 Medio', 'Académico', '2026-04-14', '2026-04-14', NULL, 4, 'Evaluación escrita sobre procesos históricos de la Independencia de Chile.'),
('Charla de Orientación Vocacional', 'Institucional', '2026-04-28', '2026-04-28', 2, 11, 'Charla para 3° y 4° medio con invitados de universidades e institutos.'),
('Reunión de apoderados 1A', 'Reunión', '2026-05-08', '2026-05-08', NULL, NULL, 'Encuentro informativo con apoderados del curso 1A.'),
('Prueba de Química 3 Medio', 'Académico', '2026-05-20', '2026-05-20', NULL, 13, 'Evaluación de unidad sobre reacciones químicas y estequiometría.'),
('Campeonato Deportivo Interescolar', 'Actividad', '2026-06-05', '2026-06-05', 3, 9, 'Jornada inaugural del campeonato de fútbol y vóleibol entre cursos.'),
('Reunión de apoderados 4 Medio', 'Reunión', '2026-06-18', '2026-06-18', NULL, NULL, 'Encuentro informativo con apoderados de los cursos de 4° medio egresados.'),
('Inicio segundo semestre', 'Institucional', '2026-07-15', '2026-07-15', NULL, NULL, 'Inicio oficial del segundo semestre escolar.'),
('Prueba de Matemáticas 4 Medio', 'Académico', '2026-07-20', '2026-07-20', NULL, 3, 'Evaluación escrita de contenidos de álgebra y funciones.'),
('Reunión de apoderados 2B', 'Reunión', '2026-07-24', '2026-07-24', NULL, NULL, 'Encuentro informativo con apoderados del curso 2B.'),
('Acto cívico institucional', 'Institucional', '2026-07-28', '2026-07-28', NULL, NULL, 'Ceremonia institucional en gimnasio techado en conmemoración de efeméride nacional.'),
('Semana de la Chilenidad — Fiestas Patrias', 'Actividad', '2026-09-14', '2026-09-18', 4, NULL, 'Cueca, ramadas internas, juegos típicos y gastronomía tradicional en todo el colegio.'),
('Feria Científica y Tecnológica', 'Actividad', '2026-10-05', '2026-10-06', 5, NULL, 'Exposición de proyectos científicos de todos los cursos en el patio central.');
