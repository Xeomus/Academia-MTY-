# Guía de pruebas — Liga MX Mongo API

Esta carpeta contiene una colección de Postman lista para probar los **9 endpoints** implementados por `TeamController`, además de dos casos negativos para comprobar las validaciones.

## Requisitos

- Java 21.
- MongoDB disponible en `localhost:27017`.
- Base de datos `ligamx` (MongoDB la crea al insertar los primeros datos).
- Postman Desktop o Web con Desktop Agent.

La aplicación usa `http://localhost:8080` por defecto y se conecta mediante:

```properties
spring.mongodb.uri=mongodb://localhost:27017/ligamx
```

## Iniciar la API

Desde `Semana2/ligaMxMongo`:

```powershell
.\gradlew.bat bootRun
```

Espere a que Spring indique que el servidor inició en el puerto 8080.

## Importar la colección

1. Abra Postman y seleccione **Import**.
2. Importe `LigaMX-Mongo.postman_collection.json`.
3. Abra la colección **Liga MX Mongo API** y revise **Variables**.
4. Conserve `baseUrl` como `http://localhost:8080`, salvo que ejecute la API en otro puerto.

`teamId` y `playerId` comienzan vacíos.

## Flujo recomendado

Ejecute manualmente en este orden:

1. **1.1 Crear equipo**: crea un equipo y guarda `teamId`.
2. **1.2 Listar equipos** y **1.3 Consultar equipo por id**.
3. **2.1 Agregar jugador**: genera un UUID y guarda `playerId`.
4. **2.2 Listar jugadores** y **2.3 Actualizar jugador**.
5. Ejecute los casos de validación si desea comprobar respuestas `400`.
6. **2.4 Eliminar jugador**.
7. **1.5 Eliminar equipo**, siempre al final.

## Endpoints

| Método | Ruta                                     | Resultado esperado                             |
| ------ | ---------------------------------------- | ---------------------------------------------- |
| GET    | `/api/teams`                             | `200`, lista de equipos                        |
| GET    | `/api/teams/{id}`                        | `200`, o `404`                                 |
| POST   | `/api/teams`                             | `200`, equipo creado                           |
| DELETE | `/api/teams/{id}`                        | `204`, o `404`                                 |
| POST   | `/api/teams/bulk`                        | `200`, lista creada                            |
| POST   | `/api/teams/{teamId}/players`            | `200`, equipo actualizado; `404` si no existe  |
| GET    | `/api/teams/{teamId}/players`            | `200`, lista de jugadores; `404` si no existe  |
| PUT    | `/api/teams/{teamId}/players/{playerId}` | `200`, jugador actualizado; `404` si no existe |
| DELETE | `/api/teams/{teamId}/players/{playerId}` | `204`, o `404`                                 |

## Formatos y validaciones

### Equipo

```json
{
  "name": "Rayados de Monterrey",
  "city": "Monterrey",
  "stadium": "Estadio BBVA",
  "foundingYear": 1945
}
```

- `name`, `city` y `stadium`: obligatorios y no pueden estar vacíos.
- `foundingYear`: obligatorio, mínimo `1800` y máximo `2100`.
- `id`: lo genera MongoDB; no es necesario enviarlo.
- `players`: opcional al crear un equipo; el modelo lo inicializa como lista vacía.

### Jugador

```json
{
  "name": "Sergio Canales",
  "number": 10,
  "position": "Mediocampista",
  "nationality": "Española"
}
```

- `name`, `position` y `nationality`: obligatorios y no pueden estar vacíos.
- `number`: obligatorio, entre `1` y `99`.
- `id`: la aplicación genera un UUID al agregar el jugador al equipo.

El endpoint `POST /api/teams/bulk` recibe una lista, pero actualmente su parámetro no utiliza `@Valid`; por ello, las validaciones del modelo podrían no ejecutarse para cada elemento del lote.

## Pruebas automáticas incluidas

Cada solicitud comprueba su código HTTP. Las pruebas también verifican la forma de las listas, los identificadores creados, la actualización del jugador y las respuestas vacías de las eliminaciones. Los dos casos negativos esperan `400 Bad Request`.

## Errores frecuentes

- **Error de conexión**: confirme que la aplicación esté ejecutándose y que `baseUrl` sea correcto.
- **Falla al iniciar Spring**: confirme que MongoDB esté activo en el puerto `27017`.
- **404 en rutas con variables**: ejecute primero **Crear equipo** y **Agregar jugador**, o escriba IDs existentes en las variables de la colección.
- **400 Bad Request**: revise los campos obligatorios y los rangos numéricos.
- **204 sin cuerpo**: es la respuesta correcta al eliminar exitosamente.
