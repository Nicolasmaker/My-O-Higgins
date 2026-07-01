# Instrucciones para correr proyecto

Este documento explica como levantar el proyecto en cualquier computador para probar el calendario escolar y la gestion de anotaciones.

La idea es que solo crees las bases de datos en MySQL Workbench y luego levantes los microservicios. El backend se encarga de crear las tablas y de cargar los datos semilla necesarios para las pruebas.

## 1. Requisitos previos

- Java 21 o superior.
- Node.js 20 o superior.
- MySQL instalado y funcionando.
- VS Code u otro editor similar.

## 2. Puertos del proyecto

- MS-Autenticacion: `8080`
- MS-Anotaciones: `8083`
- MS-HojaDeVida: `8084`
- MS-CalendarioEscolar: `8085`
- MS-GestionMatricula: `8086`
- MS-GestionAcademica: `8087`
- Frontend: `5173`

## 3. Bases de datos que debes crear

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

## 4. Orden recomendado para levantar todo

1. MySQL.
2. MS-Autenticacion.
3. MS-GestionMatricula.
4. MS-GestionAcademica.
5. MS-HojaDeVida.
6. MS-CalendarioEscolar.
7. MS-Anotaciones.
8. Frontend.

## 5. Comandos para levantar cada servicio

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

## 6. Que datos crea el backend automaticamente

- MS-Autenticacion: usuarios de prueba para login.
- MS-GestionMatricula: matriculas semilla para pruebas.
- MS-GestionAcademica: asignaturas iniciales.
- MS-HojaDeVida: hojas de vida iniciales en H2.
- MS-CalendarioEscolar: eventos iniciales.
- MS-Anotaciones: anotaciones iniciales asociadas a hojas de vida semilla.

## 7. Como probar el calendario

1. Abre `http://localhost:5173/login`.
2. Inicia sesion con un usuario de prueba.
3. Entra a `http://localhost:5173/calendario`.
4. Verifica que aparecen los eventos iniciales.
5. Si quieres, crea uno nuevo desde la pantalla.

Endpoint util:

- `GET http://localhost:8085/api/calendarios`

## 8. Como probar las anotaciones

1. Abre `http://localhost:5173/login`.
2. Inicia sesion con un usuario de prueba.
3. Entra a `http://localhost:5173/anotaciones`.
4. Verifica que aparecen las anotaciones semilla.
5. Si quieres, crea una nueva desde la pantalla.

Endpoint util:

- `GET http://localhost:8083/api/anotaciones`
- `GET http://localhost:8083/api/anotaciones/hojavida/1`

## 9. Si el puerto 8084 esta ocupado

Si `MS-HojaDeVida` no arranca porque `8084` ya esta ocupado, puedes moverlo temporalmente:

```powershell
$env:HOJA_DE_VIDA_PORT="8088"
$env:HOJA_DE_VIDA_URL="http://localhost:8088"
```

Luego vuelve a levantar el servicio. El frontend y Anotaciones ya quedan listos para apuntar a esa URL.

## 10. Resumen corto

1. Crea solo las bases de datos en MySQL Workbench.
2. Levanta los microservicios en el orden recomendado.
3. Levanta el frontend.
4. Entra a `/login`.
5. Prueba `calendario` y `anotaciones` desde el navegador.