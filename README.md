# My-O-Higgins

Proyecto semestral fullstack.

## Objetivo

Este proyecto se puede probar desde cualquier computador creando solo las bases de datos en MySQL Workbench y luego levantando los microservicios. El backend crea las tablas y los datos semilla necesarios para probar el calendario escolar y la gestion de anotaciones.

## Requisitos

- Java 21 o superior.
- Node.js 20 o superior.
- MySQL instalado y funcionando.
- VS Code u otro editor similar.

## Puertos del proyecto

- MS-Autenticacion: `8080`
- MS-Anotaciones: `8083`
- MS-HojaDeVida: `8084`
- MS-CalendarioEscolar: `8085`
- MS-GestionMatricula: `8086`
- MS-GestionAcademica: `8087`
- Frontend: `5173`

## Bases de datos a crear

Ejecuta este script en MySQL Workbench. Solo crea las bases de datos; no necesitas crear tablas ni insertar datos manualmente.

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
```

## Orden recomendado de arranque

1. MySQL.
2. MS-Autenticacion.
3. MS-GestionMatricula.
4. MS-GestionAcademica.
5. MS-HojaDeVida.
6. MS-CalendarioEscolar.
7. MS-Anotaciones.
8. Frontend.

## Microservicios que deben estar arriba para probar cada front

### Para probar Calendario

- MS-Autenticacion
- MS-GestionAcademica
- MS-CalendarioEscolar
- Frontend

### Para probar Anotaciones

- MS-Autenticacion
- MS-GestionMatricula
- MS-HojaDeVida
- MS-Anotaciones
- Frontend

## Como levantar cada modulo

Abre una terminal en la raiz del proyecto y ejecuta cada servicio en su carpeta.

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

## Que datos crea el backend automaticamente

- MS-Autenticacion: usuarios de prueba para login.
- MS-GestionMatricula: matriculas semilla para pruebas.
- MS-GestionAcademica: asignaturas iniciales.
- MS-HojaDeVida: hojas de vida iniciales en H2.
- MS-CalendarioEscolar: eventos iniciales.
- MS-Anotaciones: anotaciones iniciales asociadas a hojas de vida semilla.

## CRUD rapido con Postman

### Calendario Escolar

Base URL: `http://localhost:8085/api/calendarios`

#### Crear evento

`POST /api/calendarios`

```json
{
	"tituloEvento": "Reunion de apoderados",
	"tipoEvento": "Reunion",
	"fechaInicio": "2026-07-24",
	"fechaFin": "2026-07-24",
	"idMuralDigital": null,
	"idAsignatura": 2,
	"descripcionEvento": "Encuentro informativo con apoderados."
}
```

#### Consultar todos

`GET /api/calendarios`

#### Consultar por id

`GET /api/calendarios/{id}`

#### Actualizar evento

`PUT /api/calendarios/{id}`

```json
{
	"tituloEvento": "Reunion de apoderados actualizada",
	"tipoEvento": "Reunion",
	"fechaInicio": "2026-07-25",
	"fechaFin": "2026-07-25",
	"idMuralDigital": null,
	"idAsignatura": 2,
	"descripcionEvento": "Fecha actualizada desde Postman."
}
```

#### Eliminar evento

`DELETE /api/calendarios/{id}`

### Anotaciones

Base URL: `http://localhost:8083/api/anotaciones`

#### Crear anotacion

`POST /api/anotaciones`

```json
{
	"anotTip": "Positiva",
	"anotDes": "Excelente participacion en clase.",
	"funcionarioUsuRut": 11111111,
	"idHojaVida": 9001
}
```

#### Consultar todas

`GET /api/anotaciones`

#### Consultar por hoja de vida

`GET /api/anotaciones/hojavida/{idHojaVida}`

#### Actualizar anotacion

`PUT /api/anotaciones/{idAnot}`

```json
{
	"anotTip": "Negativa",
	"anotDes": "Se actualizo la observacion desde Postman.",
	"funcionarioUsuRut": 11111111,
	"idHojaVida": 9001
}
```

#### Eliminar anotacion

`DELETE /api/anotaciones/{idAnot}`

## Como probar el calendario

1. Abre `http://localhost:5173/login`.
2. Inicia sesion con un usuario de prueba.
3. Entra a `http://localhost:5173/calendario`.
4. Verifica que aparecen los eventos iniciales.

Endpoint util:

- `GET http://localhost:8085/api/calendarios`

## Como probar las anotaciones

1. Abre `http://localhost:5173/login`.
2. Inicia sesion con un usuario de prueba.
3. Entra a `http://localhost:5173/anotaciones`.
4. Verifica que aparecen las anotaciones semilla.

Endpoints utiles:

- `GET http://localhost:8083/api/anotaciones`
- `GET http://localhost:8083/api/anotaciones/hojavida/1`

## Si el puerto 8084 esta ocupado

Si `MS-HojaDeVida` no arranca porque `8084` ya esta ocupado, puedes moverlo temporalmente:

```powershell
$env:HOJA_DE_VIDA_PORT="8088"
$env:HOJA_DE_VIDA_URL="http://localhost:8088"
```

Luego vuelve a levantar el servicio. El frontend y Anotaciones ya quedan listos para apuntar a esa URL.
