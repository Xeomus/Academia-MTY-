# Liga MX API

API REST para administrar equipos y jugadores de la Liga MX. El proyecto utiliza Spring Boot, Spring Data JPA, validación Jakarta y MySQL.

## Requisitos

- Java 21
- MySQL
- No es necesario instalar Gradle: el repositorio incluye Gradle Wrapper.
- Postman, opcional, para importar y ejecutar la colección incluida.

## Configuración de la base de datos

1. Crea una base de datos llamada `ligamx`:

```sql
CREATE DATABASE ligamx;
```

2. Abre `src/main/resources/application.properties` y agrega tu usuario y contraseña de MySQL:

```properties
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASENA
```

La URL ya apunta a `jdbc:mysql://localhost:3306/ligamx`. Hibernate crea o actualiza las tablas automáticamente mediante `spring.jpa.hibernate.ddl-auto=update`.

## Ejecutar el proyecto

En Windows:

```powershell
.\gradlew.bat bootRun
```

En Linux o macOS:

```bash
./gradlew bootRun
```

La API queda disponible en `http://localhost:8080`.

## Modelo de datos

### Equipo

| Campo          | Tipo    | Validación                        |
| -------------- | ------- | --------------------------------- |
| `id`           | Long    | Generado automáticamente          |
| `name`         | String  | Obligatorio, no puede estar vacío |
| `city`         | String  | Obligatorio, no puede estar vacío |
| `stadium`      | String  | Obligatorio, no puede estar vacío |
| `foundingYear` | Integer | Mínimo 1800                       |
| `players`      | Array   | Jugadores asociados al equipo     |

### Jugador

| Campo         | Tipo    | Validación                        |
| ------------- | ------- | --------------------------------- |
| `id`          | Long    | Generado automáticamente          |
| `name`        | String  | Obligatorio, no puede estar vacío |
| `number`      | Integer | Obligatorio, entre 1 y 99         |
| `position`    | String  | Obligatorio, no puede estar vacío |
| `nationality` | String  | Obligatorio, no puede estar vacío |

## Endpoints

### Equipos

| Método | Ruta              | Descripción              | Respuesta esperada                            |
| ------ | ----------------- | ------------------------ | --------------------------------------------- |
| GET    | `/api/teams`      | Lista todos los equipos  | `200 OK`                                      |
| GET    | `/api/teams/{id}` | Obtiene un equipo por ID | `200 OK` o `404 Not Found`                    |
| POST   | `/api/teams`      | Crea un equipo           | `200 OK` o `400 Bad Request`                  |
| PUT    | `/api/teams/{id}` | Actualiza un equipo      | `200 OK`, `400 Bad Request` o `404 Not Found` |
| DELETE | `/api/teams/{id}` | Elimina un equipo        | `200 OK`                                      |

Ejemplo para crear o actualizar un equipo:

```json
{
  "name": "Tigres UANL",
  "city": "San Nicolas de los Garza",
  "stadium": "Estadio Universitario",
  "foundingYear": 1960
}
```

### Jugadores

| Método | Ruta                          | Descripción                      | Respuesta esperada                            |
| ------ | ----------------------------- | -------------------------------- | --------------------------------------------- |
| GET    | `/api/players`                | Lista todos los jugadores        | `200 OK`                                      |
| GET    | `/api/players/{id}`           | Obtiene un jugador por ID        | `200 OK` o `404 Not Found`                    |
| GET    | `/api/teams/{teamId}/players` | Lista los jugadores de un equipo | `200 OK`                                      |
| POST   | `/api/teams/{teamId}/players` | Crea un jugador en un equipo     | `200 OK`, `400 Bad Request` o `404 Not Found` |
| PUT    | `/api/players/{id}`           | Actualiza un jugador             | `200 OK`, `400 Bad Request` o `404 Not Found` |
| DELETE | `/api/players/{id}`           | Elimina un jugador               | `204 No Content` o `404 Not Found`            |

Ejemplo para crear o actualizar un jugador:

```json
{
  "name": "Andre-Pierre Gignac",
  "number": 10,
  "position": "Delantero",
  "nationality": "Francesa"
}
```

## Seguridad y pruebas con Postman

La API utiliza Spring Security con autenticación mediante `JWT` (JSON Web Token). 
El rol `ADMIN` tiene permisos para consultar y modificar los datos, mientras que el rol 
`VIEWER` únicamente tiene permisos de consulta.

A diferencia del proyecto anterior, donde utilizamos `HTTP Basic` y es necesario enviar las credenciales del usuario en cada petición, 
con `JWT` el usuario se autentica inicialmente enviando sus credenciales al endpoint de inicio de sesión.

Si las credenciales son correctas, la API genera un token `JWT`. 
A partir de ese momento, no es necesario volver a enviar el usuario y la contraseña en cada petición. 
En su lugar, el token generado se incluye en las siguientes solicitudes para identificar al usuario 
y validar que tenga los permisos necesarios para acceder al recurso solicitado.

| Usuario | Contraseña | Permisos |
| ------- | ---------- | -------- |
| `admin` | `admin123` | Lectura y escritura |
| `viewer` | `viewer123` | Solo lectura |

1. Inicia MySQL y ejecuta la aplicación.
2. Importa `LigaMX.postman_collection.json` en Postman.
3. Ejecuta **ADMIN / Crear equipo** y copia el `id` de la respuesta en la variable `teamId` de la colección.
4. Ejecuta **ADMIN / Crear jugador en equipo** y copia el `id` de la respuesta en `playerId`.
5. Ejecuta las carpetas **VIEWER - Permitido** y **VIEWER - Rechazado (403 Forbidden)**.

