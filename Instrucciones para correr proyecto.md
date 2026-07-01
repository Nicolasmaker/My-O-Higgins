# Instrucciones para correr proyecto

Este documento explica como levantar el proyecto en cualquier computador para probar el front, el calendario escolar y la gestion de anotaciones.

El objetivo es que puedas iniciar los microservicios y el frontend sin depender de rutas locales de otra persona.

## 1. Requisitos previos

Antes de comenzar, confirma que tienes instalado lo siguiente:

- Java 21 o superior.
- Maven o los wrappers `mvnw` de cada microservicio.
- Node.js 20 o superior.
- MySQL instalado y funcionando.
- Un editor como VS Code.

## 2. Estructura general del proyecto

El proyecto se divide en:

- `backend/MS-Autenticacion`
- `backend/MS-HojaDeVida`
- `backend/MS-GestionMatricula`
- `backend/MS-GestionAcademica`
- `backend/MS-CalendarioEscolar`
- `backend/MS-Anotaciones`
- `frontend`

## 3. Puertos que usa cada servicio

- Autenticacion: `8080`
- Anotaciones: `8083`
- HojaDeVida: `8084`
- CalendarioEscolar: `8085`
- GestionMatricula: `8086`
- GestionAcademica: `8087`
- Frontend: `5173`

## 4. Orden recomendado para levantar todo

Levanta los servicios en este orden para evitar errores de dependencia:

1. MySQL.
2. MS-Autenticacion.
3. MS-GestionMatricula.
4. MS-GestionAcademica.
5. MS-HojaDeVida.
6. MS-CalendarioEscolar.
7. MS-Anotaciones.
8. Frontend.

## 5. Base de datos y usuarios MySQL

Ejecuta este script como usuario administrador de MySQL.

```sql
CREATE DATABASE IF NOT EXISTS BD_MS_AUTENTICACION
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

CREATE DATABASE IF NOT EXISTS ms_matricula
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

CREATE DATABASE IF NOT EXISTS GESTION_ACADEMICA
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

CREATE DATABASE IF NOT EXISTS ANOTACIONES
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

CREATE USER IF NOT EXISTS 'auth_service_admin'@'localhost' IDENTIFIED BY 'Auth_2026@';
CREATE USER IF NOT EXISTS 'auth_service_admin'@'127.0.0.1' IDENTIFIED BY 'Auth_2026@';
CREATE USER IF NOT EXISTS 'matricula_service_admin'@'localhost' IDENTIFIED BY 'Matriculas_2026@';
CREATE USER IF NOT EXISTS 'matricula_service_admin'@'127.0.0.1' IDENTIFIED BY 'Matriculas_2026@';
CREATE USER IF NOT EXISTS 'gestion_acad_service_admin'@'localhost' IDENTIFIED BY 'Gestion_acad_2026@';
CREATE USER IF NOT EXISTS 'gestion_acad_service_admin'@'127.0.0.1' IDENTIFIED BY 'Gestion_acad_2026@';
CREATE USER IF NOT EXISTS 'anot_service_admin'@'localhost' IDENTIFIED BY 'Anot_2026@';
CREATE USER IF NOT EXISTS 'anot_service_admin'@'127.0.0.1' IDENTIFIED BY 'Anot_2026@';

ALTER USER 'auth_service_admin'@'localhost' IDENTIFIED BY 'Auth_2026@';
ALTER USER 'auth_service_admin'@'127.0.0.1' IDENTIFIED BY 'Auth_2026@';
ALTER USER 'matricula_service_admin'@'localhost' IDENTIFIED BY 'Matriculas_2026@';
ALTER USER 'matricula_service_admin'@'127.0.0.1' IDENTIFIED BY 'Matriculas_2026@';
ALTER USER 'gestion_acad_service_admin'@'localhost' IDENTIFIED BY 'Gestion_acad_2026@';
ALTER USER 'gestion_acad_service_admin'@'127.0.0.1' IDENTIFIED BY 'Gestion_acad_2026@';
ALTER USER 'anot_service_admin'@'localhost' IDENTIFIED BY 'Anot_2026@';
ALTER USER 'anot_service_admin'@'127.0.0.1' IDENTIFIED BY 'Anot_2026@';

GRANT ALL PRIVILEGES ON BD_MS_AUTENTICACION.* TO 'auth_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON BD_MS_AUTENTICACION.* TO 'auth_service_admin'@'127.0.0.1';
GRANT ALL PRIVILEGES ON ms_matricula.* TO 'matricula_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON ms_matricula.* TO 'matricula_service_admin'@'127.0.0.1';
GRANT ALL PRIVILEGES ON GESTION_ACADEMICA.* TO 'gestion_acad_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON GESTION_ACADEMICA.* TO 'gestion_acad_service_admin'@'127.0.0.1';
GRANT ALL PRIVILEGES ON ANOTACIONES.* TO 'anot_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON ANOTACIONES.* TO 'anot_service_admin'@'127.0.0.1';
FLUSH PRIVILEGES;
```

## 6. Que hace cada microservicio

- MS-Autenticacion: login y usuarios de prueba.
- MS-GestionMatricula: crea y consulta matriculas.
- MS-GestionAcademica: expone asignaturas para el calendario.
- MS-HojaDeVida: almacena la hoja de vida del estudiante en H2.
- MS-CalendarioEscolar: crea y consulta eventos escolares.
- MS-Anotaciones: crea, edita y elimina anotaciones asociadas a una hoja de vida.

## 7. Como levantar cada servicio

Abre una terminal para cada modulo y ejecuta el comando dentro de su carpeta.

### MS-Autenticacion

```powershell
cd backend/MS-Autenticacion
.\mvnw.cmd spring-boot:run
```

### MS-GestionMatricula

```powershell
cd backend/MS-GestionMatricula
.\mvnw.cmd spring-boot:run
```

### MS-GestionAcademica

```powershell
cd backend/MS-GestionAcademica
.\mvnw.cmd spring-boot:run
```

### MS-HojaDeVida

```powershell
cd backend/MS-HojaDeVida
.\mvnw.cmd spring-boot:run
```

### MS-CalendarioEscolar

```powershell
cd backend/MS-CalendarioEscolar
.\mvnw.cmd spring-boot:run
```

### MS-Anotaciones

```powershell
cd backend/MS-Anotaciones
.\mvnw.cmd spring-boot:run
```

### Frontend

```powershell
cd frontend
npm install
npm run dev
```

## 8. Usuarios de prueba para iniciar sesion

Si la base de datos de autenticacion esta vacia, el seeder crea usuarios de prueba.

Credenciales sugeridas:

- `admin@colegio.cl` / `admin123`
- `profe@colegio.cl` / `profe123`
- `inspector@colegio.cl` / `inspector123`
- `juan.perez@estudiante.cl` / `alumno123`
- `maria.apoderada@gmail.com` / `apoderado123`

## 9. Como probar el calendario escolar

### Paso 1: confirmar que estan arriba los servicios necesarios

- Autenticacion en `http://localhost:8080`
- GestionAcademica en `http://localhost:8087`
- CalendarioEscolar en `http://localhost:8085`
- Frontend en `http://localhost:5173`

### Paso 2: revisar que existan asignaturas

`MS-GestionAcademica` carga datos iniciales automaticamente.

Si no hubiera datos, el archivo `data.sql` crea estas asignaturas:

- Matematicas
- Lenguaje y Comunicacion
- Historia
- Ciencias Naturales

### Paso 3: abrir el front

En el navegador entra a:

- `http://localhost:5173/login`
- inicia sesion
- luego entra a `http://localhost:5173/calendario`

### Paso 4: crear un evento

Desde el formulario del calendario puedes crear eventos usando una asignatura existente.

Si quieres probar por Postman, usa:

`POST http://localhost:8085/api/calendarios`

Ejemplo de body:

```json
{
  "tituloEvento": "Reunion de apoderados",
  "tipoEvento": "Reunion",
  "fechaInicio": "2026-07-24",
  "fechaFin": "2026-07-24",
  "descripcionEvento": "Encuentro informativo con apoderados.",
  "asignaturaId": 2
}
```

### Paso 5: consultar eventos

`GET http://localhost:8085/api/calendarios`

## 10. Como probar la gestion de anotaciones

### Paso 1: confirmar servicios necesarios

- Autenticacion en `http://localhost:8080`
- GestionMatricula en `http://localhost:8086`
- HojaDeVida en `http://localhost:8084`
- Anotaciones en `http://localhost:8083`
- Frontend en `http://localhost:5173`

### Paso 2: crear una matricula

`POST http://localhost:8086/api/matriculas`

Ejemplo de body:

```json
{
  "cursoId": 1,
  "apoderadoRut": 44444444,
  "tipoAlumno": "NUEVO",
  "alumnoRut": 12345678,
  "funcionarioUsuRut": 11111111
}
```

### Paso 3: crear una hoja de vida

`POST http://localhost:8084/api/hojas-vida`

Ejemplo de body:

```json
{
  "estudianteUsuRut": 12345678,
  "matriculaId": 1
}
```

### Paso 4: crear una anotacion

`POST http://localhost:8083/api/anotaciones`

Ejemplo de body:

```json
{
  "anotTip": "Positiva",
  "anotDes": "Excelente participacion en clase.",
  "funcionarioUsuRut": 11111111,
  "idHojaVida": 1
}
```

### Paso 5: consultar anotaciones

`GET http://localhost:8083/api/anotaciones`

`GET http://localhost:8083/api/anotaciones/hojavida/1`

### Paso 6: probar desde el front

Entra a:

- `http://localhost:5173/login`
- luego `http://localhost:5173/anotaciones`

## 11. Si aparece un error de puerto ocupado

Si un microservicio no levanta porque el puerto ya esta ocupado, revisa primero si ya existe otra instancia del mismo servicio corriendo.

No cierres los otros microservicios si ya estan funcionando bien.

Para HojaDeVida puedes cambiar temporalmente el puerto con esta variable de entorno antes de levantarlo:

```powershell
$env:HOJA_DE_VIDA_PORT="8088"
$env:HOJA_DE_VIDA_URL="http://localhost:8088"
```

Si haces eso, el front seguira funcionando porque el proxy de desarrollo puede apuntar a la nueva URL.

## 12. Resumen rapido

Para probar todo desde otra computadora:

1. Crea las bases de datos y usuarios en MySQL.
2. Levanta primero los microservicios y luego el frontend.
3. Entra a `http://localhost:5173/login`.
4. Prueba el calendario en `http://localhost:5173/calendario`.
5. Prueba las anotaciones en `http://localhost:5173/anotaciones`.

1. Crear las bases de datos y usuarios de MySQL.
2. Levantar los microservicios en el orden recomendado.
3. Abrir el frontend en `http://localhost:5173`.
4. Entrar al login.
5. Probar `Calendario` en `/calendario`.
6. Probar `Anotaciones` en `/anotaciones`.
# Instrucciones para correr proyecto

Este documento explica como levantar el proyecto completo en tu propia PC para probar el **calendario** y la **gestion de anotaciones** sin depender de rutas locales de otra persona.

## 1. Requisitos previos

Antes de empezar, asegúrate de tener instalado:

- Java compatible con el proyecto
- Maven Wrapper incluido en cada microservicio
- Node.js 20 o superior
- MySQL 8 o superior
- Navegador web moderno

## 2. Puertos que usa el proyecto

- MS-Autenticacion: `8080`
- MS-Anotaciones: `8083`
- MS-HojaDeVida: `8084`
- MS-CalendarioEscolar: `8085`
- MS-GestionMatricula: `8086`
- MS-GestionAcademica: `8087`
- Frontend: `5173`

## 3. Estructura general

El proyecto se ejecuta desde la raiz donde estan las carpetas `backend` y `frontend`.

No es necesario modificar rutas de Windows ni usar rutas absolutas de otra PC. Solo abre una terminal en la raiz del proyecto y ejecuta los comandos desde ahi.

## 4. Script SQL minimo para MySQL

Ejecuta este script como usuario administrador en MySQL. Este script crea las bases de datos, usuarios y permisos necesarios. Los microservicios crean sus tablas automaticamente con JPA.

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

CREATE DATABASE IF NOT EXISTS GESTION_ACADEMICA
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

CREATE USER IF NOT EXISTS 'auth_service_admin'@'localhost' IDENTIFIED BY 'Auth_2026@';
CREATE USER IF NOT EXISTS 'auth_service_admin'@'127.0.0.1' IDENTIFIED BY 'Auth_2026@';
CREATE USER IF NOT EXISTS 'matricula_service_admin'@'localhost' IDENTIFIED BY 'Matriculas_2026@';
CREATE USER IF NOT EXISTS 'matricula_service_admin'@'127.0.0.1' IDENTIFIED BY 'Matriculas_2026@';
CREATE USER IF NOT EXISTS 'anot_service_admin'@'localhost' IDENTIFIED BY 'Anot_2026@';
CREATE USER IF NOT EXISTS 'anot_service_admin'@'127.0.0.1' IDENTIFIED BY 'Anot_2026@';
CREATE USER IF NOT EXISTS 'gestion_acad_service_admin'@'localhost' IDENTIFIED BY 'Gestion_acad_2026@';
CREATE USER IF NOT EXISTS 'gestion_acad_service_admin'@'127.0.0.1' IDENTIFIED BY 'Gestion_acad_2026@';

ALTER USER 'auth_service_admin'@'localhost' IDENTIFIED BY 'Auth_2026@';
ALTER USER 'auth_service_admin'@'127.0.0.1' IDENTIFIED BY 'Auth_2026@';
ALTER USER 'matricula_service_admin'@'localhost' IDENTIFIED BY 'Matriculas_2026@';
ALTER USER 'matricula_service_admin'@'127.0.0.1' IDENTIFIED BY 'Matriculas_2026@';
ALTER USER 'anot_service_admin'@'localhost' IDENTIFIED BY 'Anot_2026@';
ALTER USER 'anot_service_admin'@'127.0.0.1' IDENTIFIED BY 'Anot_2026@';
ALTER USER 'gestion_acad_service_admin'@'localhost' IDENTIFIED BY 'Gestion_acad_2026@';
ALTER USER 'gestion_acad_service_admin'@'127.0.0.1' IDENTIFIED BY 'Gestion_acad_2026@';

GRANT ALL PRIVILEGES ON BD_MS_AUTENTICACION.* TO 'auth_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON BD_MS_AUTENTICACION.* TO 'auth_service_admin'@'127.0.0.1';
GRANT ALL PRIVILEGES ON ms_matricula.* TO 'matricula_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON ms_matricula.* TO 'matricula_service_admin'@'127.0.0.1';
GRANT ALL PRIVILEGES ON ANOTACIONES.* TO 'anot_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON ANOTACIONES.* TO 'anot_service_admin'@'127.0.0.1';
GRANT ALL PRIVILEGES ON GESTION_ACADEMICA.* TO 'gestion_acad_service_admin'@'localhost';
GRANT ALL PRIVILEGES ON GESTION_ACADEMICA.* TO 'gestion_acad_service_admin'@'127.0.0.1';
FLUSH PRIVILEGES;
```

## 5. Orden recomendado para levantar todo

Levanta los servicios en este orden para evitar errores de dependencia:

1. MySQL
2. MS-Autenticacion
3. MS-GestionMatricula
4. MS-GestionAcademica
5. MS-HojaDeVida
6. MS-CalendarioEscolar
7. MS-Anotaciones
8. Frontend

## 6. Comandos para levantar cada modulo

Abre una terminal en la raiz del proyecto y ejecuta lo siguiente en terminales separadas.

### MS-Autenticacion

```powershell
cd backend/MS-Autenticacion
.\mvnw.cmd spring-boot:run
```

### MS-GestionMatricula

```powershell
cd backend/MS-GestionMatricula
.\mvnw.cmd spring-boot:run
```

### MS-GestionAcademica

```powershell
cd backend/MS-GestionAcademica
.\mvnw.cmd spring-boot:run
```

### MS-HojaDeVida

```powershell
cd backend/MS-HojaDeVida
.\mvnw.cmd spring-boot:run
```

### MS-CalendarioEscolar

```powershell
cd backend/MS-CalendarioEscolar
.\mvnw.cmd spring-boot:run
```

### MS-Anotaciones

```powershell
cd backend/MS-Anotaciones
.\mvnw.cmd spring-boot:run
```

### Frontend

```powershell
cd frontend
npm install
npm run dev
```

## 7. Como probar el calendario

### En el navegador

1. Abre `http://localhost:5173/login`.
2. Inicia sesion con una cuenta de prueba del sistema de autenticacion.
3. Entra a `http://localhost:5173/calendario`.
4. Verifica que se vean los eventos iniciales.
5. Crea, edita o elimina un evento desde la pantalla.

### Endpoints utiles

- `GET http://localhost:8085/api/calendarios`
- `POST http://localhost:8085/api/calendarios`
- `PUT http://localhost:8085/api/calendarios/{id}`
- `DELETE http://localhost:8085/api/calendarios/{id}`
- `GET http://localhost:8087/asignatura`

## 8. Como probar la gestion de anotaciones

### En el navegador

1. Abre `http://localhost:5173/login`.
2. Inicia sesion.
3. Entra a `http://localhost:5173/anotaciones`.
4. Crea una anotacion nueva y verifica que aparezca en el listado.

### Flujo de prueba por Postman

Si quieres probar la parte de anotaciones desde Postman, sigue este orden:

#### Paso 1: crear una matricula

`POST http://localhost:8086/api/matriculas`

```json
{
  "cursoId": 1,
  "apoderadoRut": 44444444,
  "tipoAlumno": "NUEVO",
  "alumnoRut": 12345678,
  "funcionarioUsuRut": 11111111
}
```

#### Paso 2: crear una hoja de vida

`POST http://localhost:8084/api/hojas-vida`

```json
{
  "estudianteUsuRut": 12345678,
  "matriculaId": 1
}
```

#### Paso 3: crear una anotacion

`POST http://localhost:8083/api/anotaciones`

```json
{
  "anotTip": "Positiva",
  "anotDes": "Excelente participacion en clase y colaboracion constante.",
  "funcionarioUsuRut": 11111111,
  "idHojaVida": 1
}
```

#### Paso 4: consultar anotaciones

`GET http://localhost:8083/api/anotaciones`

`GET http://localhost:8083/api/anotaciones/hojavida/1`

## 9. Datos que ya vienen listos para usar

### Autenticacion

Este microservicio crea usuarios semilla cuando la base esta vacia.

### GestionAcademica

El sistema carga asignaturas iniciales como:

- Matematicas
- Lenguaje y Comunicacion
- Historia
- Ciencias Naturales

### CalendarioEscolar

El sistema carga eventos de ejemplo asociados a esas asignaturas.

## 10. Si el puerto 8084 esta ocupado

Si `MS-HojaDeVida` no arranca porque `8084` esta ocupado, tienes dos opciones:

### Opcion A: cerrar solo el proceso viejo de HojaDeVida

Busca el proceso que usa `8084` y cierralo. Solo afecta a HojaDeVida, no al resto.

### Opcion B: mover HojaDeVida a otro puerto

Antes de levantarlo, define un puerto alternativo:

```powershell
$env:HOJA_DE_VIDA_PORT="8088"
$env:HOJA_DE_VIDA_URL="http://localhost:8088"
```

Luego levanta HojaDeVida normal. El frontend y Anotaciones ya quedan preparados para leer esa URL por configuracion.

## 11. Problemas frecuentes

- Si el login falla, revisa que MS-Autenticacion este arriba.
- Si no puedes crear matriculas, revisa MySQL y los permisos del usuario de matriculas.
- Si el calendario no muestra eventos, revisa que MS-GestionAcademica este arriba.
- Si no puedes crear anotaciones, primero asegúrate de tener matricula y hoja de vida creadas.
- Si el frontend no abre, confirma que `npm run dev` este corriendo en `5173`.

## 12. Resumen corto

Para probar todo desde tu PC:

1. Ejecuta el script SQL minimo.
2. Levanta los microservicios en el orden recomendado.
3. Abre el frontend en `http://localhost:5173`.
4. Entra a `/login`.
5. Prueba primero `calendario` y luego `anotaciones`.
