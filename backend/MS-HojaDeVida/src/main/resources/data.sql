-- Borra primero las tablas dependientes (FK hacia HOJA_VIDA_ESTUDIANTE) — si el orden se
-- invierte, un documento/antecedente cargado de verdad durante pruebas deja el DELETE de
-- HOJA_VIDA_ESTUDIANTE violando la FK en cada reinicio y el MS no levanta.
DELETE FROM DOCUMENTO_HOJA_VIDA;
DELETE FROM ANTECEDENTES_ACADEMICOS;
DELETE FROM ANTECEDENTES_APODERADO;
DELETE FROM ANTECEDENTES_MEDICOS;
DELETE FROM HOJA_VIDA_ESTUDIANTE;

INSERT INTO HOJA_VIDA_ESTUDIANTE (
  id_hoja_vida,
  ESTUDIANTE_usu_rut,
  MATRICULA_id_mat
) VALUES
(9001, 12345678, 1),
(9002, 87654321, 2),
(9003, 11223344, 3);