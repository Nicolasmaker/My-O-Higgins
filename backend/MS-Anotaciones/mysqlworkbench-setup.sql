-- MySQL Workbench setup for MS-Anotaciones
-- Ejecuta esto como usuario con permisos de administrador.

CREATE DATABASE IF NOT EXISTS ANOTACIONES
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

CREATE USER IF NOT EXISTS 'anot_service_admin'@'localhost' IDENTIFIED BY 'Anot_2026@';
CREATE USER IF NOT EXISTS 'anot_service_admin'@'127.0.0.1' IDENTIFIED BY 'Anot_2026@';
GRANT ALL PRIVILEGES ON ANOTACIONES.* TO 'anot_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON ANOTACIONES.* TO 'anot_service_admin'@'127.0.0.1';
FLUSH PRIVILEGES;

USE ANOTACIONES;

-- Verificación rápida
SELECT DATABASE() AS base_actual;
SELECT USER() AS usuario_actual;
