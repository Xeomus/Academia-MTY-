# Liga MX API con Spring Security y HTTP Basic

API REST para administrar equipos y jugadores de la Liga MX. El proyecto utiliza Spring Boot, Spring Data JPA, MySQL y Spring Security con autenticación HTTP Basic y autorización basada en permisos.

## Requisitos

- Java 21
- MySQL
- No es necesario instalar Gradle; el proyecto incluye Gradle Wrapper.

## Configuración de la base de datos

1. Crea una base de datos en MySQL:

```sql
CREATE DATABASE ligamx;
```

2. Copia `src/main/resources/application.example.properties` como `src/main/resources/application.properties`.

3. Agrega tus credenciales de MySQL:

```properties
spring.application.name=ligamx

spring.datasource.url=jdbc:mysql://localhost:3306/ligamx
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

La aplicación usa el puerto predeterminado de Spring Boot: `8080`.

## Ejecutar el proyecto

Desde esta carpeta, ejecuta en PowerShell:

```powershell
.\gradlew.bat bootRun
```

En Linux o macOS:

```bash
./gradlew bootRun
```

La API quedará disponible en `http://localhost:8080`.

## Usuarios y permisos

Al iniciar la aplicación se crean los siguientes usuarios si todavía no existen:

| Usuario | Contraseña | Rol | Permisos |
| --- | --- | --- | --- |
| `admin` | `admin123` | `ADMIN` | Consultar, crear, actualizar y eliminar equipos y jugadores |
| `viewer` | `viewer123` | `VIEWER` | Consultar equipos y jugadores |

Resumen de acceso:

| Recurso | Método | `admin` | `viewer` |
| --- | --- | ---: | ---: |
| Equipos | `GET` | 200 | 200 |
| Equipos | `POST`, `PUT`, `DELETE` | Permitido | 403 |
| Jugadores | `GET` | 200 | 200 |
| Jugadores | `POST`, `PUT`, `DELETE` | Permitido | 403 |

Una petición sin credenciales válidas recibe `401 Unauthorized`. Un usuario autenticado que no tenga el permiso requerido recibe `403 Forbidden`.

## Endpoints

### Equipos

| Método | Ruta | Descripción | Permiso |
| --- | --- | --- | --- |
| `GET` | `/api/teams` | Lista todos los equipos | `TEAM_READ` |
| `GET` | `/api/teams/{id}` | Obtiene un equipo por ID | `TEAM_READ` |
| `POST` | `/api/teams` | Crea un equipo | `TEAM_CREATE` |
| `PUT` | `/api/teams/{id}` | Actualiza un equipo | `TEAM_UPDATE` |
| `DELETE` | `/api/teams/{id}` | Elimina un equipo | `TEAM_DELETE` |

Ejemplo de equipo:

```json
{
  "name": "Tigres UANL",
  "city": "San Nicolás de los Garza",
  "stadium": "Estadio Universitario",
  "foundingYear": 1960
}
```

### Jugadores

| Método | Ruta | Descripción | Permiso |
| --- | --- | --- | --- |
| `GET` | `/api/players` | Lista todos los jugadores | `PLAYER_READ` |
| `GET` | `/api/players/{id}` | Obtiene un jugador por ID | `PLAYER_READ` |
| `GET` | `/api/teams/{teamId}/players` | Lista los jugadores de un equipo | `PLAYER_READ` |
| `POST` | `/api/teams/{teamId}/players` | Crea un jugador y lo asigna a un equipo | `PLAYER_CREATE` |
| `PUT` | `/api/players/{id}` | Actualiza un jugador | `PLAYER_UPDATE` |
| `DELETE` | `/api/players/{id}` | Elimina un jugador | `PLAYER_DELETE` |

Ejemplo de jugador:

```json
{
  "name": "André-Pierre Gignac",
  "number": 10,
  "position": "Delantero",
  "nationality": "Francesa"
}
```

## Pruebas con PowerShell

### Petición sin credenciales

```powershell
curl.exe -i http://localhost:8080/api/teams
```

Respuesta esperada: `401 Unauthorized`.

### Consultar equipos como `viewer`

```powershell
curl.exe -i -u viewer:viewer123 http://localhost:8080/api/teams
```

Respuesta esperada: `200 OK`.

### Intentar crear un equipo como `viewer`

```powershell
curl.exe -i -u viewer:viewer123 `
  -X POST http://localhost:8080/api/teams `
  -H "Content-Type: application/json" `
  -d '{"name":"Tigres UANL","city":"San Nicolás de los Garza","stadium":"Estadio Universitario","foundingYear":1960}'
```

Respuesta esperada: `403 Forbidden`.

### Crear un equipo como `admin`

```powershell
curl.exe -i -u admin:admin123 `
  -X POST http://localhost:8080/api/teams `
  -H "Content-Type: application/json" `
  -d '{"name":"Tigres UANL","city":"San Nicolás de los Garza","stadium":"Estadio Universitario","foundingYear":1960}'
```

La respuesta incluye el ID del equipo creado. Usa ese valor como `TEAM_ID` en los siguientes ejemplos.

### Crear un jugador en un equipo como `admin`

```powershell
curl.exe -i -u admin:admin123 `
  -X POST http://localhost:8080/api/teams/TEAM_ID/players `
  -H "Content-Type: application/json" `
  -d '{"name":"André-Pierre Gignac","number":10,"position":"Delantero","nationality":"Francesa"}'
```

### Consultar los jugadores de un equipo como `viewer`

```powershell
curl.exe -i -u viewer:viewer123 `
  http://localhost:8080/api/teams/TEAM_ID/players
```

## Colección de Postman

El archivo `LigaMX.postman_collection.json` contiene solicitudes preparadas para probar los endpoints con los usuarios `admin` y `viewer`. Impórtalo en Postman y ajusta las variables `teamId` y `playerId` conforme a los registros existentes.

## ¿Cómo funciona HTTP Basic?

En HTTP Basic, el cliente envía el usuario y la contraseña en cada petición mediante el encabezado Authorization:

Authorization: Basic <credenciales_codificadas>

Las credenciales se forman con el formato usuario:contraseña y posteriormente se codifican en Base64. Por ejemplo:

admin:admin123

La opción -u de curl.exe realiza este proceso automáticamente y genera el encabezado Authorization correspondiente:

```
curl.exe -u admin:admin123 http://localhost:8080/api/teams
```

Cuando Spring Security recibe la petición:

- Extrae y decodifica las credenciales del encabezado Authorization.
- Busca al usuario en la base de datos.
- Verifica la contraseña utilizando el PasswordEncoder configurado.
- Carga los roles y permisos asociados al usuario.
- Autentica al usuario si las credenciales son correctas.
- Comprueba si tiene autorización para acceder al método y la ruta solicitados.

Es importante distinguir entre autenticación y autorización:

Autenticación: comprueba quién es el usuario mediante sus credenciales.
Autorización: determina qué recursos u operaciones puede utilizar según sus roles o permisos.

A diferencia de mecanismos como `JWT`, `HTTP Basic` no genera un token de acceso después de iniciar sesión. El cliente debe enviar nuevamente sus credenciales en cada petición protegida.

- Petición 1 → usuario + contraseña
- Petición 2 → usuario + contraseña
- Petición 3 → usuario + contraseña