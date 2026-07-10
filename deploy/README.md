# Deploy — My O'Higgins (Docker en EC2)

Cada EC2 clona el repo completo y levanta **su** archivo compose. Los
contextos de build apuntan a `../backend/MS-*` y `../frontend`, así que
el repo debe estar clonado entero en cada instancia.

## Reparto de servicios

| EC2 | IP privada | Cuenta | Servicios | Compose |
|-----|-----------|--------|-----------|---------|
| SV_AUTH_ACAD | 10.0.100.254 | 1 | Autenticacion (8080), GestionAcademica (8087) | `docker-compose.auth-acad.yml` |
| SV_ANOTACIONES_HOJAVIDA | 10.0.200.61 | 1 | Anotaciones (8083), HojaDeVida (8084) | `docker-compose.anotaciones-hojavida.yml` |
| SV_REUNION_MSG | 10.1.10.253 | 2 | GestionReuniones (8081), Mensajeria (8089) | `docker-compose.reunion-msg.yml` |
| SV_CALENDARIO_MATRICULA | 10.1.10.150 | 2 | CalendarioEscolar (8085), GestionMatricula (8086) | `docker-compose.calendario-matricula.yml` |
| SV_FRONTEND | 10.0.10.49 | 1 | Frontend + Nginx (80) | `docker-compose.frontend.yml` |

## Orden de arranque recomendado

Las llamadas entre MS en tiempo de ejecución toleran que el otro esté
caído, pero **los seeders de arranque** sí tienen dependencias. Orden:

1. **SV_AUTH_ACAD** — Autenticacion siembra usuarios; es dependencia de todos.
2. **SV_CALENDARIO_MATRICULA** — GestionMatricula siembra las matrículas.
3. **SV_ANOTACIONES_HOJAVIDA** — el seeder de HojaDeVida consulta Autenticacion
   (nombres/teléfonos de apoderados) y referencia las matrículas del paso 2.
4. **SV_REUNION_MSG**
5. **SV_FRONTEND** — al final, con los MS ya arriba.

## Comandos por EC2

```bash
# Requisitos previos (una vez): schemas creados en RDS
#   CREATE DATABASE MS_AUTENTICACION;  (y el resto: ver cada application.properties)

git clone <repo> && cd My-O-Higgins/deploy

# En cada EC2, su archivo correspondiente:
docker compose -f docker-compose.auth-acad.yml up -d --build

# Ver logs / estado
docker compose -f docker-compose.auth-acad.yml logs -f
docker compose -f docker-compose.auth-acad.yml ps

# Actualizar tras un git pull
git pull
docker compose -f docker-compose.auth-acad.yml up -d --build
```

## Persistencia de datos (IMPORTANTE) — proceso de 2 fases

Los MS traen datos de siembra por 3 vías: `import.sql` (Auth, vía Hibernate),
`data.sql` (Anotaciones, CalendarioEscolar, GestionAcademica, vía Spring) y
seeders Java (`DatabaseSeeder`, con guarda `count()`). Para que **los datos que
agregues se mantengan** entre reinicios se controla con el archivo `deploy/deploy.env`:

### Fase 1 — primer arranque (siembra)
`deploy/deploy.env` viene así por defecto:
```
DDL_AUTO=create
SPRING_SQL_INIT_MODE=always
```
Levanta cada EC2 en el orden de arriba. Esto crea los esquemas, corre los scripts
de siembra y los seeders. Verifica en RDS que las tablas tengan datos.

> `create` (no `create-drop`) NO borra al apagar el contenedor, así los datos
> sobreviven cuando cambies a la fase 2.

### Fase 2 — persistencia (definitivo)
Cuando ya haya datos, edita `deploy/deploy.env` en **cada EC2**: comenta las 2 líneas
de fase 1 y descomenta:
```
DDL_AUTO=update
SPRING_SQL_INIT_MODE=never
```
y vuelve a aplicar en cada EC2:
```bash
docker compose -f docker-compose.<la-que-toca>.yml up -d
```
Desde aquí: `update` conserva tablas y datos (solo aplica cambios de esquema
aditivos), `never` evita que los scripts de siembra se repitan (no más errores de
clave duplicada), y los seeders Java se saltan solos porque ya hay datos.
**Todo lo que agregues queda guardado.**

> No dejes un MS con `create` puesto en un segundo arranque: volvería a borrar y
> recrear. El flip a fase 2 debe hacerse antes de reiniciar por segunda vez.

## Otras notas

- **URLs entre MS**: se inyectan por variable de entorno (`APP_SERVICES_*_URL`),
  sobrescribiendo el `localhost` de los `application.properties` vía relaxed
  binding de Spring Boot. No hay que tocar código.
- **Credenciales RDS**: vienen de los `application.properties`
  (`${DB_USERNAME:admin}` / `${DB_PASSWORD:...}`). Para rotarlas, descomenta
  `DB_USERNAME` / `DB_PASSWORD` en `deploy/deploy.env`.
- **Schemas RDS**: deben existir antes de arrancar (Hibernate crea las tablas
  dentro del schema, pero no el schema en sí):
  `MS_AUTENTICACION`, `GESTION_ACADEMICA`, `ANOTACIONES`, `HOJA_DE_VIDA`,
  `CALENDARIO_ESCOLAR`, `MATRICULA`, `REUNIONES`, `MENSAJERIA`.
```
