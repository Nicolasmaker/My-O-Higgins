-- Insertar País
INSERT INTO pais (pai_nom) VALUES ('Chile');

-- Insertar Región (ID 1: Metropolitana)
INSERT INTO region (id_pais, reg_nom) VALUES (1, 'Región Metropolitana de Santiago');

-- Insertar Ciudad (ID 1: Santiago)
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Santiago');

-- Insertar 10 Comunas asociadas a Santiago (ID Ciudad: 1)
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Santiago');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Providencia');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Las Condes');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Vitacura');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Ñuñoa');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'La Florida');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Maipú');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Puente Alto');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Recoleta');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Quilicura');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Independencia');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'San Miguel');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'La Granja');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'La Cisterna');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'Lo Prado');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'San Joaquin');
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'El Bosque');
