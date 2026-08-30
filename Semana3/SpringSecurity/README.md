# Spring Security — Semana 3

Este directorio contiene tres ejemplos independientes de seguridad con Spring Boot:

| Proyecto | Seguridad | Base de datos | Puerto |
|---|---|---|---:|
| `ligamxSecurityHttpBasic` | HTTP Basic | MySQL, base `ligamx` | `8080` |
| `ligamxSecurityJWT` | JWT emitido por la propia API | MySQL, base `ligamx` | `8080` |
| `Security-oauth2-keycloak` | OAuth2 Resource Server con Keycloak | MySQL, base `employee_directory` | `8073` |

## Requisitos

- Java 21.
- MySQL 8 o compatible, local o en Docker.
- PowerShell en Windows.
- Docker, solo para el ejemplo con Keycloak.

## 1. Liga MX con HTTP Basic

Crea la base en MySQL:

```sql
CREATE DATABASE IF NOT EXISTS ligamx;
```

Desde la raíz de este repositorio, crea la configuración local:

```powershell
Set-Location .\ligamxSecurityHttpBasic
Copy-Item .\src\main\resources\application.example.properties `
  .\src\main\resources\application.properties
```

Edita `application.properties` y agrega el usuario y la contraseña de MySQL.
Después arranca la aplicación:

```powershell
.\gradlew.bat bootRun
```

La API estará disponible en `http://localhost:8080/api/teams`.

| Usuario | Contraseña | Acceso |
|---|---|---|
| `admin` | `admin123` | Lectura y escritura |
| `viewer` | `viewer123` | Solo lectura |

Prueba rápida:

```powershell
curl.exe -i -u viewer:viewer123 http://localhost:8080/api/teams
```

Consulta [la guía del proyecto](ligamxSecurityHttpBasic/README.md) para ver todos
los endpoints y ejemplos.

## 2. Liga MX con JWT

Detén primero el proyecto HTTP Basic si sigue usando el puerto `8080`.
Este proyecto también usa la base `ligamx`.

Desde la raíz del repositorio:

```powershell
Set-Location .\ligamxSecurityJWT
Copy-Item .\src\main\resources\application.example.properties `
  .\src\main\resources\application.properties
```

Completa las credenciales de MySQL en `application.properties` y define un
secreto JWT de al menos 32 bytes antes de arrancar:

```powershell
$env:JWT_SECRET = "p7Kx3mQ9vL2nR8wT5yH1cF6jD0sA4zUeB9iN2oG7qXk="
.\gradlew.bat bootRun
```

La API estará disponible en `http://localhost:8080`. El inicio de sesión se hace
en `POST /api/auth/login` y devuelve el JWT que debe enviarse como Bearer token.

Consulta [la guía del proyecto](ligamxSecurityJWT/README.md) para obtener un token
y probar los permisos de `admin` y `viewer`.

## 3. OAuth2 con Keycloak

Este ejemplo necesita tres servicios:

| Servicio | Dirección |
|---|---|
| MySQL | `localhost:3306` |
| Keycloak | `http://localhost:8090` |
| API Spring Boot | `http://localhost:8073` |

La configuración incluida utiliza la base `employee_directory` y el usuario
MySQL `springstudent`, con contraseña `springstudent`:

```sql
CREATE DATABASE IF NOT EXISTS employee_directory;
CREATE USER IF NOT EXISTS 'springstudent'@'%' IDENTIFIED BY 'springstudent';
GRANT ALL PRIVILEGES ON employee_directory.* TO 'springstudent'@'%';
FLUSH PRIVILEGES;
```

Sigue la sección de inicialización de
[la guía de Keycloak](Security-oauth2-keycloak/README.md). Ahí se documentan la
creación del contenedor y el script que prepara el realm `academy`, sus roles y
sus usuarios.

Para arrancar Spring Boot desde la raíz del repositorio:

```powershell
Set-Location .\Security-oauth2-keycloak
.\mvnw.cmd spring-boot:run
```

La API estará disponible en `http://localhost:8073/api/employees`.

## Orden recomendado de arranque

Para HTTP Basic o JWT:

1. Arranca MySQL.
2. Verifica `application.properties`.
3. Define `JWT_SECRET` si usarás JWT.
4. Ejecuta el wrapper del proyecto.

Para OAuth2:

1. Arranca MySQL.
2. Arranca Keycloak y comprueba que el realm `academy` exista.
3. Ejecuta `Security-oauth2-keycloak` con `mvnw.cmd`.

## Diferencias entre los ejemplos

- **HTTP Basic:** el cliente envía usuario y contraseña en cada petición.
- **JWT propio:** el cliente inicia sesión una vez y reutiliza el token emitido
  por la API.
- **OAuth2 + Keycloak:** Keycloak autentica y emite el token; Spring Boot solo lo
  valida y aplica los roles.

En los tres casos, `401 Unauthorized` indica que falta una autenticación válida;
`403 Forbidden` indica que el usuario está autenticado, pero no tiene permisos
para realizar la operación.
