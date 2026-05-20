# MS-HojaDeVida

Microservicio simple de Hoja de Vida para pruebas locales y consumo desde otros servicios.

## Ejecución local

En Windows:

```powershell
cd backend/MS-HojaDeVida
./mvnw.cmd spring-boot:run
```

En Linux / macOS:

```bash
cd backend/MS-HojaDeVida
./mvnw spring-boot:run
```

## Endpoints

- `GET /api/hojas-vida`
- `GET /api/hojas-vida/{idHojaVida}`
- `POST /api/hojas-vida`
- `PUT /api/hojas-vida/{idHojaVida}`
- `DELETE /api/hojas-vida/{idHojaVida}`

## Ejemplo POST / PUT

```json
{
  "estudianteUsuRut": 12345678,
  "matriculaId": 1
}
```

## Pruebas manuales en Postman

### Crear hoja de vida

- Método: `POST`
- URL: `http://localhost:8083/api/hojas-vida`
- Body JSON:

```json
{
  "estudianteUsuRut": 12345678,
  "matriculaId": 1
}
```

### Listar todas las hojas de vida

- Método: `GET`
- URL: `http://localhost:8083/api/hojas-vida`

### Consultar hoja de vida por ID

- Método: `GET`
- URL: `http://localhost:8083/api/hojas-vida/1`

### Actualizar hoja de vida

- Método: `PUT`
- URL: `http://localhost:8083/api/hojas-vida/1`
- Body JSON:

```json
{
  "estudianteUsuRut": 12345678,
  "matriculaId": 2
}
```

### Eliminar hoja de vida

- Método: `DELETE`
- URL: `http://localhost:8083/api/hojas-vida/1`

## Notas

- El microservicio corre en el puerto `8083`.
- Usa `./mvnw spring-boot:run` en Linux/macOS o `./mvnw.cmd spring-boot:run` en Windows.
- El proyecto ya incluye pruebas automáticas para `POST`, `GET`, `PUT` y `DELETE` en `HojaDeVidaApplicationTests`.
