# My O'Higgins - Guia de pruebas para MS-Anotaciones

Este documento explica como levantar el front y los microservicios necesarios para probar el login y el alta de anotaciones desde el navegador o desde Postman.

## 1. Que debes tener corriendo

### Servicios requeridos
- MySQL local
- MS-Autenticacion en `http://localhost:8080`
- MS-HojaDeVida en `http://localhost:8084`
- MS-GestionMatricula en `http://localhost:8086`
- MS-Anotaciones en `http://localhost:8083`
- Frontend en `http://localhost:5173`

### Orden recomendado de arranque
1. Levanta MySQL.
2. Levanta MS-Autenticacion.
3. Levanta MS-GestionMatricula.
4. Levanta MS-HojaDeVida.
5. Levanta MS-Anotaciones.
6. Levanta el frontend.

## 2. Comandos para levantar cada modulo

### MS-Autenticacion
```powershell
cd "C:\Users\perez\OneDrive\Escritorio\Evaluacion fullstack\My-O-Higgins\backend\MS-Autenticacion"
.\mvnw.cmd spring-boot:run
```

### MS-GestionMatricula
```powershell
cd "C:\Users\perez\OneDrive\Escritorio\Evaluacion fullstack\My-O-Higgins\backend\MS-GestionMatricula"
.\mvnw.cmd spring-boot:run
```

### MS-HojaDeVida
```powershell
cd "C:\Users\perez\OneDrive\Escritorio\Evaluacion fullstack\My-O-Higgins\backend\MS-HojaDeVida"
.\mvnw.cmd spring-boot:run
```

### MS-Anotaciones
```powershell
cd "C:\Users\perez\OneDrive\Escritorio\Evaluacion fullstack\My-O-Higgins\backend\MS-Anotaciones"
.\mvnw.cmd spring-boot:run
```

### Frontend
```powershell
cd "C:\Users\perez\OneDrive\Escritorio\Evaluacion fullstack\My-O-Higgins\frontend"
npm install
npm run dev
```

## 3. Login para entrar al front

La pagina de login esta en:
- `http://localhost:5173/login`

Credenciales de prueba creadas por el seeder de Autenticacion:
- `admin@colegio.cl` / `admin123`
- `profe@colegio.cl` / `profe123`
- `inspector@colegio.cl` / `inspector123`
- `juan.perez@estudiante.cl` / `alumno123`
- `maria.apoderada@gmail.com` / `apoderado123`

Despues de iniciar sesion, entra a:
- `http://localhost:5173/anotaciones`

## 4. Datos que deben existir para poder crear una anotacion

Para que el formulario de Anotaciones funcione, deben existir estos registros:

### A. Autenticacion
Tablas MySQL necesarias:
- `usuarios`
- `funcionario`
- `rol`

Estos datos ya se crean automaticamente con `DatabaseSeeder` al levantar MS-Autenticacion, si la base esta vacia.

Usuarios de prueba que crea el seeder:
- RUT `11111111` -> Directivo
- RUT `22222222` -> Docente
- RUT `33333333` -> Inspector
- RUT `12345678` -> Estudiante
- RUT `44444444` -> Apoderado

### B. GestionMatricula
Tabla MySQL necesaria:
- `MATRICULA`

Para probar anotaciones, primero debes tener una matricula creada porque la hoja de vida usa `matriculaId`.

### C. Hoja de Vida
MS-HojaDeVida usa H2 local, no MySQL.
No necesitas crear una tabla manualmente en MySQL para este modulo.
La entidad que se crea en H2 es:
- `HOJA_VIDA_ESTUDIANTE`

### D. Anotaciones
Tabla MySQL necesaria:
- `anotaciones`

## 5. Script SQL minimo para MySQL

Ejecuta esto como usuario administrador en MySQL.

```sql
CREATE DATABASE IF NOT EXISTS BD_MS_AUTENTICACION
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

CREATE DATABASE IF NOT EXISTS ms_matricula
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

CREATE DATABASE IF NOT EXISTS ANOTACIONES
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

CREATE USER IF NOT EXISTS 'auth_service_admin'@'localhost' IDENTIFIED BY 'Auth_2026@';
CREATE USER IF NOT EXISTS 'auth_service_admin'@'127.0.0.1' IDENTIFIED BY 'Auth_2026@';
CREATE USER IF NOT EXISTS 'matricula_service_admin'@'localhost' IDENTIFIED BY 'Matriculas_2026@';
CREATE USER IF NOT EXISTS 'matricula_service_admin'@'127.0.0.1' IDENTIFIED BY 'Matriculas_2026@';
CREATE USER IF NOT EXISTS 'anot_service_admin'@'localhost' IDENTIFIED BY 'Anot_2026@';
CREATE USER IF NOT EXISTS 'anot_service_admin'@'127.0.0.1' IDENTIFIED BY 'Anot_2026@';

ALTER USER 'auth_service_admin'@'localhost' IDENTIFIED BY 'Auth_2026@';
ALTER USER 'auth_service_admin'@'127.0.0.1' IDENTIFIED BY 'Auth_2026@';
ALTER USER 'matricula_service_admin'@'localhost' IDENTIFIED BY 'Matriculas_2026@';
ALTER USER 'matricula_service_admin'@'127.0.0.1' IDENTIFIED BY 'Matriculas_2026@';
ALTER USER 'anot_service_admin'@'localhost' IDENTIFIED BY 'Anot_2026@';
ALTER USER 'anot_service_admin'@'127.0.0.1' IDENTIFIED BY 'Anot_2026@';

GRANT ALL PRIVILEGES ON BD_MS_AUTENTICACION.* TO 'auth_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON BD_MS_AUTENTICACION.* TO 'auth_service_admin'@'127.0.0.1';
GRANT ALL PRIVILEGES ON ms_matricula.* TO 'matricula_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON ms_matricula.* TO 'matricula_service_admin'@'127.0.0.1';
GRANT ALL PRIVILEGES ON ANOTACIONES.* TO 'anot_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON ANOTACIONES.* TO 'anot_service_admin'@'127.0.0.1';
FLUSH PRIVILEGES;

USE BD_MS_AUTENTICACION;

CREATE TABLE IF NOT EXISTS usuarios (
  usu_rut INT NOT NULL,
  usu_dv_rut CHAR(1) NOT NULL,
  usu_p_nombre VARCHAR(100) NOT NULL,
  usu_s_nombre VARCHAR(100) NULL,
  usu_ape_pat VARCHAR(50) NOT NULL,
  usu_ape_mat VARCHAR(50) NOT NULL,
  usu_email VARCHAR(100) NOT NULL,
  usu_password VARCHAR(90) NOT NULL,
  usu_tel VARCHAR(13) NOT NULL,
  usu_estado_actividad BIT(1) NOT NULL,
  PRIMARY KEY (usu_rut),
  UNIQUE KEY uk_usuarios_email (usu_email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS funcionario (
  usu_rut INT NOT NULL,
  fun_titulo VARCHAR(50) NOT NULL,
  PRIMARY KEY (usu_rut),
  CONSTRAINT fk_funcionario_usuario
    FOREIGN KEY (usu_rut) REFERENCES usuarios(usu_rut)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS rol (
  id_rol BIGINT NOT NULL AUTO_INCREMENT,
  usu_rut INT NOT NULL,
  rol_nom VARCHAR(30) NOT NULL,
  PRIMARY KEY (id_rol),
  UNIQUE KEY uk_rol_usuario (usu_rut),
  CONSTRAINT fk_rol_usuario
    FOREIGN KEY (usu_rut) REFERENCES usuarios(usu_rut)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

USE ms_matricula;

CREATE TABLE IF NOT EXISTS MATRICULA (
  id_mat BIGINT NOT NULL AUTO_INCREMENT,
  curso_id BIGINT NULL,
  apoderado_rut BIGINT NULL,
  tipo_alumno VARCHAR(20) NULL,
  mat_fec DATE NOT NULL,
  mat_est VARCHAR(50) NOT NULL,
  mat_anio_acad INT NOT NULL,
  alu_rut BIGINT NOT NULL,
  FUNCIONARIO_usu_rut BIGINT NOT NULL,
  PRIMARY KEY (id_mat)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

USE ANOTACIONES;

CREATE TABLE IF NOT EXISTS anotaciones (
  id_anot BIGINT NOT NULL AUTO_INCREMENT,
  anot_tip VARCHAR(15) NOT NULL,
  anot_des VARCHAR(1000) NOT NULL,
  anot_fec DATE NOT NULL,
  funcionario_usu_rut BIGINT NOT NULL,
  id_hoja_vida BIGINT NOT NULL,
  PRIMARY KEY (id_anot),
  KEY idx_anotaciones_funcionario (funcionario_usu_rut),
  KEY idx_anotaciones_hoja_vida (id_hoja_vida)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

## 6. Flujo para probar alta de anotacion desde el front

### Paso 1: crear una matricula por Postman
`POST http://localhost:8086/api/matriculas`

Body:
```json
{
  "cursoId": 1,
  "apoderadoRut": 44444444,
  "tipoAlumno": "NUEVO",
  "alumnoRut": 12345678,
  "funcionarioUsuRut": 11111111
}
```

### Paso 2: verificar la matricula
`GET http://localhost:8086/api/matriculas`

### Paso 3: crear una hoja de vida por Postman
`POST http://localhost:8084/api/hojas-vida`

Body:
```json
{
  "estudianteUsuRut": 12345678,
  "matriculaId": 1
}
```

### Paso 4: verificar la hoja de vida
`GET http://localhost:8084/api/hojas-vida`

### Paso 5: crear una anotacion desde el front o Postman
`POST http://localhost:8083/api/anotaciones`

Body:
```json
{
  "anotTip": "Positiva",
  "anotDes": "Excelente participacion en clase y colaboracion constante.",
  "funcionarioUsuRut": 11111111,
  "idHojaVida": 1
}
```

### Paso 6: verificar la anotacion
`GET http://localhost:8083/api/anotaciones`

`GET http://localhost:8083/api/anotaciones/hojavida/1`

## 7. Ruta para probar el front

Una vez que todo este arriba, abre:
- `http://localhost:5173/login`
- inicia sesion
- luego entra a `http://localhost:5173/anotaciones`

## 8. Si algo falla

- Si login falla, revisa que MS-Autenticacion este arriba y que MySQL tenga la base `BD_MS_AUTENTICACION`.
- Si no puedes crear matriculas, revisa que el usuario `matricula_service_admin` exista y tenga permisos sobre `ms_matricula`.
- Si no puedes crear anotaciones, revisa que exista una hoja de vida valida antes de intentar guardar.
- Si el front no abre, revisa que Vite este corriendo en el puerto que te muestre `npm run dev`.

## 9. Resumen corto

Para probar el alta desde el front necesitas:
1. Autenticacion arriba y con usuarios semilla.
2. Matricula creada.
3. Hoja de vida creada.
4. MS-Anotaciones arriba.
5. Frontend arriba.
6. Entrar a `/login` y luego a `/anotaciones`.
