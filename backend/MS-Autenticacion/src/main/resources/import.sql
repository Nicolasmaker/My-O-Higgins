-- Insertar País (ID: 1)
INSERT INTO pais (pai_nom) VALUES ('Chile');

-- Insertar Región (ID: 1)
INSERT INTO region (id_pais, reg_nom) VALUES (1, 'Coquimbo');

-- ========================================================
-- Insertar las 15 Ciudades Cabeceras
-- ========================================================
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'La Serena');       -- ID 1
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Coquimbo');        -- ID 2
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Andacollo');       -- ID 3
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'La Higuera');      -- ID 4
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Paihuano');        -- ID 5
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Vicuña');          -- ID 6
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Ovalle');          -- ID 7
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Combarbalá');      -- ID 8
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Monte Patria');    -- ID 9
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Punitaqui');       -- ID 10
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Río Hurtado');     -- ID 11
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Illapel');         -- ID 12
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Canela');          -- ID 13
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Los Vilos');       -- ID 14
INSERT INTO ciudad (id_reg, ciu_nom) VALUES (1, 'Salamanca');       -- ID 15

-- ========================================================
-- Insertar las 15 Comunas (Relación 1 a 1 con su ciudad)
-- ========================================================
INSERT INTO comuna (id_ciu, com_nom) VALUES (1, 'La Serena');
INSERT INTO comuna (id_ciu, com_nom) VALUES (2, 'Coquimbo');
INSERT INTO comuna (id_ciu, com_nom) VALUES (3, 'Andacollo');
INSERT INTO comuna (id_ciu, com_nom) VALUES (4, 'La Higuera');
INSERT INTO comuna (id_ciu, com_nom) VALUES (5, 'Paihuano');
INSERT INTO comuna (id_ciu, com_nom) VALUES (6, 'Vicuña');
INSERT INTO comuna (id_ciu, com_nom) VALUES (7, 'Ovalle');
INSERT INTO comuna (id_ciu, com_nom) VALUES (8, 'Combarbalá');
INSERT INTO comuna (id_ciu, com_nom) VALUES (9, 'Monte Patria');
INSERT INTO comuna (id_ciu, com_nom) VALUES (10, 'Punitaqui');
INSERT INTO comuna (id_ciu, com_nom) VALUES (11, 'Río Hurtado');
INSERT INTO comuna (id_ciu, com_nom) VALUES (12, 'Illapel');
INSERT INTO comuna (id_ciu, com_nom) VALUES (13, 'Canela');
INSERT INTO comuna (id_ciu, com_nom) VALUES (14, 'Los Vilos');
INSERT INTO comuna (id_ciu, com_nom) VALUES (15, 'Salamanca');