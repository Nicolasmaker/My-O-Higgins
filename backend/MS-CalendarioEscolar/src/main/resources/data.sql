INSERT INTO MURAL_DIGITAL (
  mur_dig_titulo,
  mur_dig_contenido,
  mur_dig_fec_pub,
  FUNCIONARIO_usu_rut
) VALUES
('Bienvenida segundo semestre', 'Damos la bienvenida a toda la comunidad escolar al segundo semestre 2026. Revisar horarios actualizados en secretaría.', '2026-07-10', 11111111),
('Campaña de vacunación', 'El día 30 de julio se realizará la campaña de vacunación escolar en la enfermería del establecimiento.', '2026-07-12', 11111111);

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
('Acto cívico institucional', 'Actividad', '2026-07-28', '2026-07-28', NULL, 3, 'Ceremonia institucional en gimnasio techado.');