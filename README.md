# My-O-Higgins

Sistema Integral de Gestión Estudiantil Digital para el Colegio Bernardo O'Higgins (Coquimbo, Chile). Proyecto académico Duoc UC (DSY1106) — arquitectura de microservicios (backend) + SPA (frontend).

## Stack

| Capa | Tecnología |
|---|---|
| Backend | Java 25 + Spring Boot 4.0.6 + Spring Data JPA (Hibernate) |
| Base de datos | MySQL 8.0 (mayoría de MSes) + H2 (CalendarioEscolar, HojaDeVida) |
| Frontend | React 19 + Vite 8 + React Router 7 |
| UI | Bootstrap 5 + react-bootstrap + CSS Modules |
| Formularios | React Hook Form 7 |
| HTTP | Axios (con interceptor JWT + redirect en 401) |
| Notificaciones | React Toastify |

## Requisitos previos

- **Java 25** o superior (JDK).
- **Node.js 20** o superior + npm.
- **MySQL 8.0** instalado y corriendo en `localhost:3306`.
- Los microservicios traen Maven Wrapper (`mvnw.cmd`) — no necesitas instalar Maven aparte.

## Estructura del proyecto

```
My-O-Higgins/
├── backend/
│   ├── MS-Autenticacion      (8080) — login, usuarios, roles, JWT
│   ├── MS-GestionReuniones   (8081) — bitácoras de reuniones (apoderado/individual/general)
│   ├── MS-Anotaciones        (8083) — anotaciones positivas/negativas
│   ├── MS-HojaDeVida         (8084) — hoja de vida + antecedentes (H2)
│   ├── MS-CalendarioEscolar  (8085) — eventos escolares + mural digital (H2)
│   ├── MS-GestionMatricula   (8086) — matrículas
│   ├── MS-GestionAcademica   (8087) — cursos, asignaturas, notas, evaluaciones
│   ├── MS-Mensajeria         (8089) — mensajería interna
│   └── MS-Reportes           (vacío — Fase IV, sin implementar)
└── frontend/                 (5173) — SPA React, proxy Vite hacia todos los MSes
```

## Bases de datos a crear

Solo necesitas **crear las bases y sus usuarios**. Cada microservicio crea sus propias tablas automáticamente al arrancar (Hibernate `ddl-auto`), y algunos cargan datos semilla desde `data.sql`.

`MS-CalendarioEscolar` y `MS-HojaDeVida` usan **H2** (no requieren MySQL ni pasos manuales).

Ejecuta en MySQL Workbench o consola `mysql`:

```sql
-- MS-Autenticacion
CREATE DATABASE IF NOT EXISTS BD_MS_AUTENTICACION CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
CREATE USER IF NOT EXISTS 'auth_service_admin'@'localhost' IDENTIFIED BY 'Auth_2026@';
GRANT ALL PRIVILEGES ON BD_MS_AUTENTICACION.* TO 'auth_service_admin'@'localhost';

-- MS-GestionReuniones
CREATE DATABASE IF NOT EXISTS REUNIONES CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
CREATE USER IF NOT EXISTS 'reuniones_service_admin'@'localhost' IDENTIFIED BY 'Reuniones_2026@';
GRANT ALL PRIVILEGES ON REUNIONES.* TO 'reuniones_service_admin'@'localhost';

-- MS-Anotaciones
CREATE DATABASE IF NOT EXISTS ANOTACIONES CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
CREATE USER IF NOT EXISTS 'anot_service_admin'@'localhost' IDENTIFIED BY 'Anot_2026@';
GRANT ALL PRIVILEGES ON ANOTACIONES.* TO 'anot_service_admin'@'localhost';

-- MS-GestionMatricula
CREATE DATABASE IF NOT EXISTS ms_matricula CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
CREATE USER IF NOT EXISTS 'matricula_service_admin'@'localhost' IDENTIFIED BY 'Matriculas_2026@';
GRANT ALL PRIVILEGES ON ms_matricula.* TO 'matricula_service_admin'@'localhost';

-- MS-GestionAcademica
CREATE DATABASE IF NOT EXISTS GESTION_ACADEMICA CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
CREATE USER IF NOT EXISTS 'gestion_acad_service_admin'@'localhost' IDENTIFIED BY 'Gestion_acad_2026@';
GRANT ALL PRIVILEGES ON GESTION_ACADEMICA.* TO 'gestion_acad_service_admin'@'localhost';

-- MS-Mensajeria
CREATE DATABASE IF NOT EXISTS MENSAJERIA CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
CREATE USER IF NOT EXISTS 'msg_service_admin'@'localhost' IDENTIFIED BY 'Msg_2026@';
GRANT ALL PRIVILEGES ON MENSAJERIA.* TO 'msg_service_admin'@'localhost';

FLUSH PRIVILEGES;
```

> Las credenciales ya están escritas en cada `application.properties` de su MS — este script solo replica esos usuarios en tu MySQL local. Son valores de **desarrollo**, no usar en producción.

### Resumen de bases y usuarios

| MS | Base de datos | Usuario | Password | Motor |
|---|---|---|---|---|
| MS-Autenticacion | `BD_MS_AUTENTICACION` | `auth_service_admin` | `Auth_2026@` | MySQL |
| MS-GestionReuniones | `REUNIONES` | `reuniones_service_admin` | `Reuniones_2026@` | MySQL |
| MS-Anotaciones | `ANOTACIONES` | `anot_service_admin` | `Anot_2026@` | MySQL |
| MS-GestionMatricula | `ms_matricula` | `matricula_service_admin` | `Matriculas_2026@` | MySQL |
| MS-GestionAcademica | `GESTION_ACADEMICA` | `gestion_acad_service_admin` | `Gestion_acad_2026@` | MySQL |
| MS-Mensajeria | `MENSAJERIA` | `msg_service_admin` | `Msg_2026@` | MySQL |
| MS-HojaDeVida | archivo `./data/hojadevida-db` | `sa` (sin password) | — | H2 (se crea solo) |
| MS-CalendarioEscolar | en memoria `calendariodb` | `sa` (sin password) | — | H2 (se recrea cada arranque) |

## Puertos

| Servicio | Puerto |
|---|---|
| MS-Autenticacion | `8080` |
| MS-GestionReuniones | `8081` |
| MS-Anotaciones | `8083` |
| MS-HojaDeVida | `8084` (override con `HOJA_DE_VIDA_PORT`) |
| MS-CalendarioEscolar | `8085` |
| MS-GestionMatricula | `8086` |
| MS-GestionAcademica | `8087` |
| MS-Mensajeria | `8089` |
| Frontend (Vite) | `5173` |

## Orden recomendado de arranque

1. MySQL (con las bases del script de arriba ya creadas).
2. MS-Autenticacion — **prerequisito de todos los demás** (Reuniones, Matrícula, Académico y HojaDeVida validan RUTs contra este MS vía `RestClient`).
3. MS-GestionReuniones
4. MS-GestionMatricula
5. MS-GestionAcademica
6. MS-HojaDeVida
7. MS-CalendarioEscolar
8. MS-Anotaciones
9. MS-Mensajeria
10. Frontend

En desarrollo no es obligatorio tener todos arriba: cada página del frontend solo necesita su(s) propio(s) MS + Autenticación (para el login). Si un MS está caído, esa página muestra un mensaje de error sin romper el resto de la app.

## Cómo levantar cada módulo

Abre una terminal por cada servicio, parado en la raíz del repo.

```powershell
# MS-Autenticacion
cd backend/MS-Autenticacion
.\mvnw.cmd spring-boot:run

# MS-GestionReuniones
cd backend/MS-GestionReuniones
.\mvnw.cmd spring-boot:run

# MS-Anotaciones
cd backend/MS-Anotaciones
.\mvnw.cmd spring-boot:run

# MS-HojaDeVida
cd backend/MS-HojaDeVida
.\mvnw.cmd spring-boot:run

# MS-CalendarioEscolar
cd backend/MS-CalendarioEscolar
.\mvnw.cmd spring-boot:run

# MS-GestionMatricula
cd backend/MS-GestionMatricula
.\mvnw.cmd spring-boot:run

# MS-GestionAcademica
cd backend/MS-GestionAcademica
.\mvnw.cmd spring-boot:run

# MS-Mensajeria
cd backend/MS-Mensajeria
.\mvnw.cmd spring-boot:run

# Frontend
cd frontend
npm install
npm run dev
```

Luego abre `http://localhost:5173`.

## Qué datos crea el backend automáticamente

- **MS-Autenticacion**: usuarios de prueba para login (roles ESTUDIANTE/DOCENTE/INSPECTOR/DIRECTIVO/APODERADO).
- **MS-GestionMatricula**: matrículas semilla.
- **MS-GestionAcademica**: asignaturas iniciales.
- **MS-HojaDeVida**: hojas de vida iniciales.
- **MS-CalendarioEscolar**: eventos y publicaciones de mural digital iniciales.
- **MS-Anotaciones**: anotaciones iniciales asociadas a hojas de vida semilla.
- **MS-GestionReuniones** y **MS-Mensajeria**: parten vacíos, se prueban creando registros desde el frontend o Postman.

## Rutas del frontend

| Ruta | Página | Requiere sesión |
|---|---|---|
| `/` | Home | No |
| `/login` | Login | No |
| `/anotaciones` | Anotaciones | Sí |
| `/calendario` | Calendario Escolar | Sí |
| `/reuniones` | Bitácora de Reuniones | Sí |
| `/mensajeria` | Mensajería interna | Sí |
| `/matriculas` | Registro de Matrículas | Sí |
| `/hoja-de-vida` | Hoja de Vida del Estudiante | Sí |
| `/academico` | Gestión Académica (o "Mis notas" si el rol es Estudiante) | Sí |

Todas las rutas protegidas redirigen a `/login` si no hay sesión activa (`ProtectedRoute`).

## Si el puerto 8084 está ocupado

```powershell
$env:HOJA_DE_VIDA_PORT="8088"
$env:HOJA_DE_VIDA_URL="http://localhost:8088"
```

Vuelve a levantar `MS-HojaDeVida`. El frontend ya está preparado para leer `HOJA_DE_VIDA_URL` en el proxy de Vite.

## Pendiente por hacer

### Crítico

- **Prueba end-to-end con backends reales** — todo el frontend se verificó visualmente con datos simulados (mocks); falta levantar los 8 MSes + MySQL juntos y probar el flujo real login → CRUD en cada página.
- **`SecurityConfig` en modo dev** (`anyRequest().permitAll()`) en todos los MSes — activar los roles reales antes de producción. Ojo: activarlo hoy rompe las llamadas cross-MS (Reuniones/Matrícula/Académico/HojaDeVida llaman a `MS-Autenticacion` vía `RestClient` sin adjuntar token), hay que resolver eso primero (service token o ruta interna exenta).

### Backend

- **MS-Reportes** — microservicio vacío, corresponde a Fase IV del proyecto (sin requisitos definidos aún).
- **MURAL_DIGITAL** — CRUD implementado en `MS-CalendarioEscolar` (`/api/murales`), pero **sin página en el frontend** todavía (solo el service `calendarioService.js` está listo).

### Frontend

- **Certificados** — la tarjeta del Home apunta a `#`; no existe módulo de certificados.
- **Validación de RUT** — se valida formato y dígito verificador (módulo 11) cuando el usuario lo incluye, pero no se verifica contra un padrón real; cualquier RUT bien formado pasa aunque no exista (el backend sí valida existencia contra MS-Autenticacion en Reuniones/Matrícula/Académico).
- Limpiar bundle: Vite avisa que el build final supera 470 KB — se puede dividir en chunks por ruta (`React.lazy`) si se vuelve un problema real.

## Notas de arquitectura (no tocar sin razón)

- `ddl-auto=create-drop` en Autenticación, Reuniones, Académico y Mensajería es intencional en desarrollo (recrea tablas en cada arranque). **No cambiar a `update`** salvo que sepas que vas a perder los datos semilla.
- Relaciones cross-MS se guardan como columnas planas (`Integer`/`Long`), nunca `@ManyToOne` — cada MS tiene su propia base de datos independiente.
- Tablas de rompimiento del modelo original (REALIZAR, TUTELAR, ASISTIR, IMPARTIR, TENER, ENVIAR) no están implementadas; el sistema funciona sin ellas.
