# My-O-Higgins

---

LINK de la pagina: http://98.89.45.152/  solo se ve con la sesion de aws abierta, sin embargo en la rama de feat/conexion-backend-bdAWS esta el despliegue.
Si desea ver el proyecto lea este README para saber como correrlo en local host desde la rama Main.

---

## 1. Introducción y contexto

**My-O-Higgins** es una plataforma web de gestión escolar desarrollada para el Colegio Bernardo O'Higgins (Coquimbo, Chile), en el marco del proyecto académico de la carrera de Ingenieria Informatica (asignatura Fullstack3). Su propósito es digitalizar y centralizar los procesos administrativos y académicos que tradicionalmente se llevan en papel o en sistemas dispersos: la matrícula de estudiantes, el seguimiento de calificaciones y asistencia, el registro de anotaciones de convivencia, la coordinación de reuniones entre docentes y apoderados, la comunicación interna y la publicación de información institucional.

El sistema reúne en un único espacio digital a los distintos actores de la comunidad escolar —estudiantes, apoderados, docentes, inspectores y directivos— ofreciendo a cada uno una vista adaptada a su rol y a las funciones que le corresponden.

## 2. Objetivos del sistema

- **Centralizar la información académica**: calificaciones, evaluaciones, asignaturas, cursos y asistencia accesibles desde un único portal.
- **Digitalizar el proceso de matrícula**: desde la solicitud del apoderado hasta la aprobación del directivo, incluyendo la creación de cuentas para estudiantes y apoderados nuevos.
- **Trazabilidad de la convivencia escolar**: registro formal de anotaciones positivas y negativas, con gravedad y vínculo a la hoja de vida del estudiante.
- **Coordinación institucional**: agendamiento y firma de actas de reuniones, mensajería interna y un mural digital de avisos.
- **Control de acceso por rol**: cada usuario ve y opera únicamente lo que su rol le permite.

## 3. Actores del sistema (roles)

El sistema define cinco roles, cada uno con un conjunto de permisos y vistas propias:

| Rol | Descripción y capacidades principales |
|---|---|
| **Directivo** | Máximo nivel administrativo. Gestiona matrículas, funcionarios, cursos y toda la información académica. Aprueba o rechaza solicitudes de matrícula. |
| **Docente** | Ingresa notas y asistencia de sus cursos, registra anotaciones de sus estudiantes, agenda reuniones y consulta hojas de vida. |
| **Inspector** | Enfocado en convivencia escolar. Registra anotaciones para cualquier estudiante y participa en reuniones. |
| **Apoderado** | Consulta la información académica y las anotaciones de su(s) pupilo(s), solicita matrículas, recibe mensajes y confirma reuniones. |
| **Estudiante** | Consulta sus propias notas, evaluaciones, curso, sala y anotaciones. |

El control de acceso se aplica en dos niveles: el frontend oculta las secciones no permitidas y el backend valida el rol antes de procesar operaciones sensibles.

## 4. Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Java 25 + Spring Boot 4.0.6 + Spring Data JPA (Hibernate) |
| Seguridad | Spring Security + JSON Web Tokens (JWT, algoritmo HS256) |
| Base de datos | MySQL 8.0 (mayoría de servicios) + H2 (HojaDeVida y CalendarioEscolar) |
| Frontend | React 19 + Vite 8 + React Router 7 |
| Interfaz | Bootstrap 5 + react-bootstrap + CSS Modules |
| Formularios | React Hook Form 7 |
| Comunicación HTTP | Axios (con interceptores JWT) + RestClient (entre microservicios) |
| Notificaciones | React Toastify |
| Documentación de API | Swagger / OpenAPI 3 |

## 5. Arquitectura general

El sistema adopta una **arquitectura de microservicios**: el backend se divide en ocho servicios independientes, cada uno responsable de un dominio de negocio acotado y con su propia base de datos. El frontend es una **Single Page Application (SPA)** que consume las ocho APIs a través de un proxy de desarrollo (Vite), presentándose al usuario como una aplicación única y cohesionada.

Las características arquitectónicas centrales son:

- **Una base de datos por servicio**: ningún microservicio accede a la base de datos de otro; toda comunicación ocurre vía HTTP.
- **Comunicación entre servicios mediante clientes REST**: cuando un servicio necesita un dato de otro (por ejemplo, el nombre del curso de un estudiante), lo solicita por HTTP a la API del servicio dueño de ese dato.
- **Tolerancia a fallos parciales**: las llamadas entre servicios son *best-effort* —si un servicio dependiente no responde, la información se muestra parcialmente en vez de fallar por completo.
- **Seguridad centralizada y replicada**: un único servicio emite los tokens JWT; los demás los validan mediante un filtro de seguridad idéntico y compartido.

## 6. Descripción de los microservicios

| Microservicio | Puerto | Responsabilidad |
|---|---|---|
| **MS-Autenticacion** | 8080 | Núcleo de identidad. Gestiona usuarios (estudiantes, apoderados, funcionarios), roles y datos geográficos (país, región, comuna, dirección). Emite y valida el JWT. Es prerrequisito de todos los demás servicios. |
| **MS-GestionReuniones** | 8081 | Bitácoras de reuniones individuales (docente–apoderado) y generales (por curso), con estados de confirmación y firmas separadas por parte. |
| **MS-Anotaciones** | 8083 | Anotaciones positivas y negativas de estudiantes, con nivel de gravedad y vínculo a la hoja de vida. |
| **MS-HojaDeVida** | 8084 | Expediente integral del estudiante: antecedentes académicos, médicos y de apoderado. Genera PDF descargable. |
| **MS-CalendarioEscolar** | 8085 | Eventos del calendario institucional y publicaciones del mural digital. |
| **MS-GestionMatricula** | 8086 | Proceso de matrícula, solicitudes de apoderados y su aprobación. Sincroniza el curso del estudiante y crea automáticamente su hoja de vida. |
| **MS-GestionAcademica** | 8087 | Cursos, asignaturas, niveles, salas, evaluaciones, notas, asistencia y la relación docente–asignatura–curso ("Impartir"). |
| **MS-Mensajeria** | 8089 | Mensajería interna entre usuarios del sistema. |

## 7. Módulos funcionales

### 7.1. Autenticación y sesión

El usuario ingresa con su correo institucional y contraseña. El sistema valida las credenciales, emite un token JWT firmado que incluye el rol del usuario, y lo almacena en el navegador para mantener la sesión activa entre recargas. Al iniciar sesión, el usuario aterriza en la página de inicio, que muestra el **Mural Digital** personalizado.

### 7.2. Inicio — Mural Digital

Para el usuario público (sin sesión), la portada muestra información institucional, accesos rápidos y noticias. Una vez autenticado, la portada se transforma en un **tablero personalizado por rol** que reúne: las próximas evaluaciones que le corresponden, los próximos eventos del calendario relevantes para su curso o función, y los avisos generales del mural (talleres, campañas, actividades) visibles para todos.

### 7.3. Gestión Académica

Es el módulo más extenso. Para el **directivo**, ofrece la administración completa (crear, editar, eliminar) de siete entidades: cursos, asignaturas, niveles, salas, evaluaciones, bitácoras de asignatura y notas. Para el **docente**, ofrece vistas propias: "Mis Estudiantes", "Pasar Lista" (registro de asistencia por curso, asignatura y fecha) e "Ingresar Notas" (selección de curso y evaluación, seguido de una planilla del roster de estudiantes con cálculo de estado Aprobado/Reprobado en vivo). Para **estudiantes y apoderados**, ofrece una vista de solo lectura de notas, evaluaciones, asignaturas y curso.

### 7.4. Gestión de Matrícula

El **directivo** puede registrar matrículas de dos maneras: mediante un **asistente guiado** ("wizard") para estudiantes completamente nuevos —que crea las cuentas del apoderado y del estudiante paso a paso— o mediante un **registro rápido** para cuentas ya existentes. El sistema controla los **cupos disponibles por curso**, calculados a partir de la capacidad de la sala asignada menos las matrículas activas.

Los **apoderados** pueden enviar **solicitudes de matrícula** desde su propio portal, indicando el estudiante y el curso deseado; el directivo las revisa y las aprueba —eligiendo la sección definitiva del curso, con visibilidad de los cupos— o las rechaza con un motivo. Al concretarse una matrícula, se crea automáticamente la hoja de vida del estudiante y se sincroniza su curso en el servicio de autenticación.

### 7.5. Anotaciones

Docentes e inspectores registran anotaciones **positivas** o **negativas** (estas últimas con gravedad: Leve, Grave o Muy Grave), asociadas a la hoja de vida del estudiante. El listado se puede filtrar por tipo, gravedad, curso (lista desplegable) y por RUT o nombre del estudiante. El docente ve únicamente las anotaciones de los estudiantes de sus cursos; el inspector y el directivo ven todas. Desde una anotación negativa se puede **vincular una citación**, que genera una reunión individual pre-llenada con el apoderado.

### 7.6. Reuniones

Permite agendar **reuniones individuales** (docente–apoderado, opcionalmente originadas en una anotación) y **reuniones generales** de curso, seleccionando fecha y tipo. Las reuniones individuales cuentan con un mecanismo de **firma separada**: el funcionario firma su sección y el apoderado la suya, sin que uno pueda firmar por el otro. Se puede completar el acta con los temas tratados, compromisos y observaciones.

### 7.7. Hoja de Vida

Expediente completo del estudiante, con antecedentes académicos, médicos y de apoderado. Directivos y funcionarios pueden visualizar y editar cada dato; el estudiante y el apoderado pueden consultar la información y **descargar el PDF** del expediente.

### 7.8. Calendario Escolar

Muestra los eventos institucionales (pruebas, reuniones, actos, actividades) en vistas de mes, semana y lista, con filtros. Los eventos pueden estar asociados a una asignatura o a un mural, y su visibilidad se ajusta según el rol y el curso del usuario.

### 7.9. Mensajería

Cliente de correo interno: lista de mensajes recibidos y panel de lectura, con la posibilidad de redactar y enviar mensajes a otros usuarios del sistema, identificados por su RUT.

### 7.10. Gestión de Funcionarios

Exclusivo del directivo, permite crear y administrar las cuentas de los funcionarios del colegio (docentes, inspectores y otros directivos).

## 8. Modelo de datos (aspectos destacados)

Cada microservicio posee su propio esquema. Algunos puntos relevantes del modelo:

- **Identidad y geografía**: el usuario (`Usuario`) se especializa en Funcionario (a su vez Docente, Inspector o Directivo), Estudiante y Apoderado. El RUT se almacena separando el cuerpo numérico del dígito verificador. Los datos de dirección se normalizan mediante entidades geográficas (País → Región → Ciudad → Comuna → Dirección).
- **Relaciones entre microservicios**: como cada servicio tiene su base de datos, las referencias a entidades de otros servicios se guardan como **columnas planas** (por ejemplo, un `estudianteUsuRut` o un `cursoId` numérico), nunca como claves foráneas de base de datos. La resolución del dato completo se hace en tiempo de ejecución vía llamadas HTTP.
- **La relación "Impartir"**: en Gestión Académica, vincula a un docente con una asignatura y un curso, y es la base para determinar qué puede ver y hacer cada docente.
- **Cupos**: el curso incorpora un campo de cupos, y la disponibilidad se calcula dinámicamente contra las matrículas activas.

## 9. Seguridad

La seguridad se sustenta en **JSON Web Tokens**. MS-Autenticacion es el único emisor: tras validar las credenciales, firma un token que incluye el rol del usuario como *claim*, de modo que los demás servicios pueden conocer el rol del portador sin volver a consultar la base de datos de usuarios.

Cada microservicio incorpora un **filtro de validación de JWT** que intercepta las peticiones entrantes, verifica la firma y la vigencia del token, y establece la identidad del usuario en el contexto de seguridad. La activación de esta protección está controlada por un **interruptor de configuración** (`app.security.enabled`), lo que permite alternar entre un modo abierto para desarrollo y un modo protegido para producción sin modificar código.

Los valores sensibles —secreto de firma del JWT, credenciales de base de datos, comportamiento de errores— están **externalizados como variables de entorno**, de manera que el mismo código funciona en desarrollo con valores por defecto y en producción con valores seguros inyectados por el entorno.

## 10. Frontend: navegación y rutas

| Ruta | Página | Acceso |
|---|---|---|
| `/` | Inicio (Mural Digital si hay sesión) | Público / Autenticado |
| `/login` | Inicio de sesión | Público |
| `/anotaciones` | Anotaciones | Autenticado |
| `/calendario` | Calendario Escolar | Autenticado |
| `/reuniones` | Reuniones | Autenticado |
| `/mensajeria` | Mensajería | Autenticado |
| `/matriculas` | Matrículas | Directivo, Apoderado |
| `/hoja-de-vida` | Hoja de Vida | Autenticado |
| `/academico` | Gestión Académica | Estudiante, Apoderado, Docente, Directivo |
| `/funcionarios` | Gestión de Funcionarios | Directivo |

La navegación se protege mediante un componente guardián (`ProtectedRoute`) que redirige al login si no hay sesión, o al inicio si el rol no tiene permiso para la ruta solicitada.

## 11. Puesta en marcha

### 11.1. Requisitos a instalar en un equipo nuevo

En un computador nuevo solo es necesario instalar **manualmente** tres herramientas base:

- **JDK (Java 25 o superior)** — para compilar y ejecutar los microservicios.
- **Node.js 20 o superior (incluye npm)** — para el frontend.
- **MySQL 8.0** — motor de base de datos, corriendo en `localhost:3306`.

**No es necesario instalar Maven, Bootstrap, jsPDF ni ninguna otra librería por separado**, porque el proyecto gestiona sus propias dependencias automáticamente:

- En el **backend**, cada microservicio incluye el *Maven Wrapper* (`mvnw`), que descarga Maven y todas las librerías de Java (Spring Boot, Spring Security, driver de MySQL, jjwt, etc.) la primera vez que se ejecuta.
- En el **frontend**, un único comando `npm install` lee el archivo `package.json` y descarga de una sola vez **todas** las dependencias declaradas: React, React Router, **Bootstrap** y react-bootstrap (interfaz), **jsPDF y jsPDF-AutoTable** (generación del PDF de la hoja de vida), Axios, React Hook Form, React Toastify, entre otras. No se instalan una por una.

### 11.2. Preparación de la base de datos

Antes del primer arranque se deben **crear las bases de datos y sus usuarios** en MySQL (los servicios crean por sí solos sus tablas al arrancar y cargan datos semilla de prueba realistas y consistentes entre sí). MS-HojaDeVida y MS-CalendarioEscolar usan H2 y no requieren ninguna preparación manual.

### 11.3. Ejecución

El frontend se prepara una única vez con `npm install` (dentro de la carpeta `frontend/`) y luego se levanta con `npm run dev` (Vite). Cada microservicio se levanta de forma independiente, abriendo una terminal por servicio, parado en su carpeta, con el comando:

```powershell
.\mvnw.cmd spring-boot:run
```

### 11.4. Orden de encendido recomendado

El orden importa: **MS-Autenticacion debe estar completamente arriba antes que el resto**, ya que varios servicios sincronizan datos contra él al arrancar (por ejemplo, la matrícula sincroniza el curso del estudiante y crea su hoja de vida consultando a Autenticación y a HojaDeVida).

1. **MySQL** (con las bases de datos y usuarios ya creados).
2. **MS-Autenticacion** (8080) — prerrequisito de todos los demás.
3. **MS-HojaDeVida** (8084) — debe estar arriba antes que Matrícula, porque Matrícula la invoca para crear la hoja de vida.
4. **MS-GestionAcademica** (8087) — provee los cursos que consultan Matrícula y Anotaciones.
5. **MS-GestionMatricula** (8086).
6. **MS-GestionReuniones** (8081).
7. **MS-CalendarioEscolar** (8085).
8. **MS-Anotaciones** (8083).
9. **MS-Mensajeria** (8089).
10. **Frontend** (`npm run dev`, puerto 5173).

En desarrollo no es estrictamente obligatorio tener los ocho servicios arriba a la vez: cada página del frontend solo necesita su(s) propio(s) microservicio(s) más Autenticación (para el login). Si un servicio está caído, esa página muestra un mensaje de error sin romper el resto de la aplicación.

## 12. Estado del proyecto y trabajo futuro

El sistema se encuentra funcional en sus módulos principales. Las líneas de trabajo futuro identificadas incluyen:

- **Módulo de Reportes** (MS-Reportes): planificado como fase posterior, aún sin implementar.
- **Estrategia de tokens de refresco**: para permitir sesiones prolongadas de forma segura sin re-autenticación manual.
- **Autenticación servicio-a-servicio**: para cerrar por completo las rutas internas que hoy quedan abiertas para permitir la comunicación entre microservicios.
- **Certificados**: módulo de emisión de certificados, contemplado pero no desarrollado.

---

## 13. Síntesis

My-O-Higgins es una plataforma escolar completa que cubre el ciclo administrativo y académico de un establecimiento educativo, construida sobre una arquitectura de microservicios que separa cada dominio de negocio en un servicio autónomo. El sistema equilibra una interfaz de usuario adaptada por rol y visualmente coherente con un backend modular, seguro y resiliente, sentando una base tecnológica sólida y extensible para la digitalización de la gestión escolar.

---

👩‍💻 Autores

Benjamin Aravena
Estudiante de Ingenieria en Informatica mencion Desarrollo Software– Duoc UC

Francisca Guerrero
Estudiante de Ingenieria en Informatica mencion Desarrollo Software– Duoc UC

Francisca Lopez
Estudiante de Ingenieria en Informatica mencion Desarrollo Software– Duoc UC

Nicolas Perez
Estudiante de Ingenieria en Informatica mencion Desarrollo Software– Duoc UC
