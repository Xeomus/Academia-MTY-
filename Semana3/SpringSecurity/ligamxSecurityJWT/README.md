# Liga MX API con Spring Security y JWT

API REST para administrar equipos y jugadores de la Liga MX. El proyecto usa Spring Boot, Spring Security, JWT, Spring Data JPA y MySQL.

Todas las operaciones sobre equipos y jugadores requieren un token JWT. Los permisos incluidos en el token determinan si el usuario puede consultar o modificar los recursos.

## Requisitos

- Java 21
- MySQL

## Configuración

1. Crea en MySQL una base de datos llamada `ligamx`:

   ```sql
   CREATE DATABASE ligamx;
   ```

2. Copia el archivo de ejemplo:

   ```powershell
   Copy-Item src/main/resources/application.example.properties `
     src/main/resources/application.properties
   ```

3. Completa en `application.properties` las credenciales de MySQL:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ligamx
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_CONTRASEÑA
   ```

4. Define `JWT_SECRET` antes de iniciar la aplicación. Para HS256 conviene utilizar un secreto aleatorio de al menos 32 bytes.

   ```powershell
   $env:JWT_SECRET = "p7Kx3mQ9vL2nR8wT5yH1cF6jD0sA4zUeB9iN2oG7qXk="
   ```

`spring.jpa.hibernate.ddl-auto=update` crea o actualiza las tablas al arrancar.

## Ejecución

En Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

En Linux o macOS:

```bash
./gradlew bootRun
```

La aplicación utiliza el puerto predeterminado de Spring Boot: `http://localhost:8080`.

## Usuarios iniciales

Al iniciar la aplicación, `SecurityDataInitializer` registra estos usuarios si todavía no existen:

| Usuario | Contraseña | Rol | Acceso |
|---|---|---|---|
| `admin` | `admin123` | `ADMIN` | Consultar, crear, actualizar y eliminar |
| `viewer` | `viewer123` | `VIEWER` | Solo consultar |

Estas credenciales son únicamente para desarrollo y demostración.

## Autenticación

El login recibe las credenciales como JSON; no usa HTTP Basic:

```powershell
$login = curl.exe -s `
  -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{"username":"admin","password":"admin123"}' |
  ConvertFrom-Json

$token = $login.token
```

La respuesta tiene esta forma:

```json
{
  "token": "eyJ..."
}
```

Las siguientes peticiones deben enviar el token como Bearer:

```powershell
curl.exe -i `
  -H "Authorization: Bearer $token" `
  http://localhost:8080/api/teams
```

Un login con credenciales incorrectas devuelve `401 Unauthorized`. Una petición a un recurso protegido sin un JWT válido también devuelve `401`. Un usuario autenticado que no tiene el permiso requerido recibe `403 Forbidden`.

## Ejemplos con el usuario administrador

### Crear un equipo

```powershell
$team = curl.exe -s `
  -X POST http://localhost:8080/api/teams `
  -H "Authorization: Bearer $token" `
  -H "Content-Type: application/json" `
  -d '{"name":"Tigres UANL","city":"San Nicolas de los Garza","stadium":"Estadio Universitario","foundingYear":1960}' |
  ConvertFrom-Json

$teamId = $team.id
```

Los campos `name`, `city` y `stadium` son obligatorios. `foundingYear` debe ser igual o posterior a 1800.

### Crear un jugador dentro del equipo

```powershell
$player = curl.exe -s `
  -X POST "http://localhost:8080/api/teams/$teamId/players" `
  -H "Authorization: Bearer $token" `
  -H "Content-Type: application/json" `
  -d '{"name":"Andre-Pierre Gignac","number":10,"position":"Delantero","nationality":"Francesa"}' |
  ConvertFrom-Json

$playerId = $player.id
```

`name`, `position` y `nationality` son obligatorios. `number` debe estar entre 1 y 99.

### Actualizar y eliminar un jugador

```powershell
curl.exe -i `
  -X PUT "http://localhost:8080/api/players/$playerId" `
  -H "Authorization: Bearer $token" `
  -H "Content-Type: application/json" `
  -d '{"name":"Andre-Pierre Gignac","number":10,"position":"Delantero centro","nationality":"Francesa"}'

curl.exe -i `
  -X DELETE "http://localhost:8080/api/players/$playerId" `
  -H "Authorization: Bearer $token"
```

## Comprobar los permisos de VIEWER

```powershell
$viewerToken = (curl.exe -s `
  -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{"username":"viewer","password":"viewer123"}' |
  ConvertFrom-Json).token
```

El usuario puede consultar equipos:

```powershell
curl.exe -i `
  -H "Authorization: Bearer $viewerToken" `
  http://localhost:8080/api/teams
```

Pero no puede crearlos; esta petición devuelve `403 Forbidden`:

```powershell
curl.exe -i `
  -X POST http://localhost:8080/api/teams `
  -H "Authorization: Bearer $viewerToken" `
  -H "Content-Type: application/json" `
  -d '{"name":"Equipo de prueba","city":"Monterrey","stadium":"Estadio de prueba","foundingYear":2000}'
```

## ¿Como funciona JWT?

`JWT` (JSON Web Token) es un mecanismo de autenticación basado en tokens. A diferencia de `HTTP Basic`, donde el usuario y la contraseña se envían en cada petición, con `JWT` las credenciales se envían principalmente una vez, durante el inicio de sesión.

Cuando el usuario realiza el login, el servidor verifica el usuario y la contraseña. Si las credenciales son correctas, genera un `JWT` y se lo devuelve al cliente. A partir de ese momento, el cliente utiliza ese token para acceder a los recursos protegidos, enviándolo en el encabezado `Authorization` con el formato `Bearer <token>`.

Cuando el cliente realiza una petición con el `JWT`, Spring Security valida que el token tenga una firma correcta, que no haya expirado y que represente a un usuario autorizado. Si el token es válido, permite continuar con la petición según los roles y permisos correspondientes.

## Colección de Postman

El archivo `LigaMX.postman_collection.json` incluye peticiones para iniciar sesión como `ADMIN` y `VIEWER`, guardar los tokens automáticamente y probar los permisos de todos los endpoints.
