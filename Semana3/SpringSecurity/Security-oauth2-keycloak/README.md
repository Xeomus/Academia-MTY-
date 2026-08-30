# OAuth2 Resource Server con Keycloak

Este proyecto expone una API REST de empleados protegida con OAuth2 y JWT.
Keycloak autentica a los usuarios y emite los tokens; Spring Boot funciona
únicamente como **Resource Server**: recibe el JWT, valida su firma y aplica
las reglas de autorización.

## Arquitectura y puertos

| Servicio | Dirección desde Windows | Puerto interno | Propósito |
|---|---|---:|---|
| MySQL | `localhost:3306` | `3306` | Almacena los empleados |
| Keycloak | `http://localhost:8090` | `8080` | Autentica usuarios y emite JWT |
| Spring Boot | `http://localhost:8073` | `8073` | Expone `/api/employees` |

El mapeo `8090:8080` de Docker significa que Keycloak escucha en el puerto
`8080` dentro del contenedor, pero se accede desde la computadora mediante el
puerto `8090`.

```text
Cliente
  |
  | 1. usuario y contraseña
  v
Keycloak :8090
  |
  | 2. access token (JWT)
  v
Cliente
  |
  | 3. Authorization: Bearer <JWT>
  v
Spring Boot :8073  ------>  MySQL :3306
```

## Requisitos

- Java 21
- Maven
- Docker
- MySQL en ejecución
- Base de datos `employee_directory`
- Usuario de MySQL `springstudent` con contraseña `springstudent`
- Git Bash para ejecutar los scripts `.sh` en Windows

La conexión y los puertos están definidos en
`src/main/resources/application.properties`.

## Inicialización del proyecto

### 1. Preparar MySQL

Antes de iniciar Spring Boot, comprueba que MySQL esté escuchando en el puerto
`3306` y que exista la base de datos:

```sql
CREATE DATABASE IF NOT EXISTS employee_directory;
```

La aplicación utiliza JPA, pero el proyecto no incluye un script de creación ni
datos iniciales para la tabla de empleados. La base debe estar preparada antes
de probar los endpoints.

### 2. Crear y arrancar Keycloak

La primera vez, crea el contenedor desde PowerShell:

```powershell
docker run --name keycloak-academy -p 8090:8080 `
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin `
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin `
  -d quay.io/keycloak/keycloak:26.4 start-dev
```

Si la imagen todavía no está disponible localmente:

```powershell
docker pull quay.io/keycloak/keycloak:26.4
```

En ejecuciones posteriores no vuelvas a crear el contenedor. Arráncalo con:

```powershell
docker start keycloak-academy
```

Puedes revisar su estado y sus logs con:

```powershell
docker ps
docker logs keycloak-academy
```

Comprueba que Keycloak responde:

```powershell
curl.exe http://localhost:8090/realms/master/.well-known/openid-configuration
```

La consola administrativa está en `http://localhost:8090` y las credenciales
iniciales son `admin` / `admin`.

### 3. Configurar el realm `academy`

El script `scripts/keycloak-setup.sh` crea:

- El realm `academy`.
- El cliente público `employee-api`.
- Los roles `EMPLOYEE`, `MANAGER` y `ADMIN`.
- Los usuarios de prueba y sus asignaciones de roles.

Abre **Git Bash en la raíz de este repositorio** y ejecuta:

```bash
MSYS_NO_PATHCONV=1 ./scripts/keycloak-setup.sh
```

`MSYS_NO_PATHCONV=1` evita que Git Bash convierta la ruta Linux
`/opt/keycloak/bin/kcadm.sh`, utilizada dentro del contenedor, en una ruta de
Windows.

Si fuera necesario, utiliza esta alternativa:

```bash
export MSYS2_ARG_CONV_EXCL="*"
./scripts/keycloak-setup.sh
```

Comprueba que el realm quedó disponible:

```powershell
curl.exe http://localhost:8090/realms/academy/.well-known/openid-configuration
```

La respuesta debe contener:

```json
"issuer": "http://localhost:8090/realms/academy"
```

Si recibes `Realm does not exist`, el script no terminó correctamente.

### 4. Arrancar Spring Boot

Desde la raíz del repositorio ejecuta:

```powershell
mvn spring-boot:run
```

La API queda disponible en:

```text
http://localhost:8073/api/employees
```
## ¿Cómo funciona Keycloak con Spring Boot?

`Keycloak` funciona como un **proveedor de identidad `(Identity Provider)`**. Su función es centralizar la autenticación de los usuarios y la administración de identidades, roles y permisos.

En este proyecto, `Spring Boot` funciona como un **Resource Server**: no administra directamente las credenciales de los usuarios ni genera los tokens, sino que confía en los tokens emitidos por `Keycloak`.

### 1. Keycloak autentica

Cuando un usuario necesita autenticarse, las credenciales son procesadas por `Keycloak` y despues verifica la identidad del usuario y administra sus roles y permisos.

Esto permite separar la autenticación de la lógica de la aplicación. Además, varias aplicaciones pueden configurarse para utilizar el mismo proveedor de identidad, lo que permite implementar **Single Sign-On (SSO)**: el usuario puede iniciar sesión una vez y acceder a distintas aplicaciones que confían en el mismo sistema de autenticación.

`Keycloak` utiliza estándares como **OAuth 2.0** y **OpenID Connect (OIDC)** para realizar este proceso.

### 2. Keycloak emite el JWT

Si la autenticación es válida, `Keycloak` puede emitir un **access token**, normalmente en formato `JWT`. Este token contiene *claims* con información necesaria para identificar al usuario y determinar sus permisos.

Por ejemplo:

```json
{
  "iss": "http://localhost:8090/realms/academy",
  "preferred_username": "john",
  "realm_access": {
    "roles": ["EMPLOYEE"]
  }
}
```

El token está firmado por `Keycloak`, por lo que la API puede comprobar que realmente fue emitido por una fuente en la que confía y que su contenido no fue modificado.

### 3. Spring valida el JWT

La siguiente propiedad establece el emisor de tokens en el que confía la API:

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8090/realms/academy
```

Spring Security utiliza la configuración publicada por Keycloak mediante `OpenID` Connect para localizar las llaves públicas disponibles en el **JWKS (JSON Web Key Set)**.

Con estas llaves puede validar la firma del `JWT` y comprobar aspectos como su emisor y vigencia.

De esta manera, la API no necesita guardar las contraseñas de los usuarios, realizar directamente el login ni emitir sus propios `JWT`. Su responsabilidad es **validar los access tokens recibidos y proteger los recursos de la aplicación**.

### 4. Spring aplica los roles

Keycloak almacena los roles del usuario dentro de `realm_access.roles`. El convertidor configurado en `SecurityConfig` transforma estos roles al formato de autoridades utilizado por Spring Security:

```text
EMPLOYEE -> ROLE_EMPLOYEE
MANAGER  -> ROLE_MANAGER
ADMIN    -> ROLE_ADMIN
```

A partir de estas autoridades, Spring Security determina si el usuario puede realizar la operación solicitada.

Los permisos configurados son:

| Operación | Endpoint | Rol requerido |
|---|---|---|
| Consultar | `GET /api/employees` | `EMPLOYEE` |
| Consultar uno | `GET /api/employees/{id}` | `EMPLOYEE` |
| Crear | `POST /api/employees` | `MANAGER` |
| Actualizar | `PUT /api/employees` | `MANAGER` |
| Actualizar parcialmente | `PATCH /api/employees/{id}` | `MANAGER` |
| Eliminar | `DELETE /api/employees/{id}` | `ADMIN` |

Usuarios creados por el script:

| Usuario | Contraseña | Roles |
|---|---|---|
| `john` | `test123` | `EMPLOYEE` |
| `mary` | `test123` | `EMPLOYEE`, `MANAGER` |
| `susan` | `test123` | `EMPLOYEE`, `MANAGER`, `ADMIN` |

En resumen, `Keycloak` autentica al usuario y emite el token, mientras que `Spring Boot` valida ese token y determina si el usuario tiene los permisos necesarios para acceder al recurso solicitado.

## Probar la API

### Petición sin token

```powershell
curl.exe -i http://localhost:8073/api/employees
```

El resultado esperado es `401 Unauthorized`: no se presentó autenticación.

### Obtener un token

Ejemplo con `john` desde PowerShell:

```powershell
$tokenResponse = curl.exe -s `
  -X POST "http://localhost:8090/realms/academy/protocol/openid-connect/token" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "client_id=employee-api" `
  -d "username=john" `
  -d "password=test123" `
  -d "grant_type=password" |
  ConvertFrom-Json

$TOKEN = $tokenResponse.access_token
```

### Usar el token

```powershell
curl.exe -i `
  -H "Authorization: Bearer $TOKEN" `
  http://localhost:8073/api/employees
```

Con un token válido de `john`, el `GET` devuelve `200 OK`. Si `john` intenta
crear o eliminar un empleado obtiene `403 Forbidden`: está autenticado, pero no
tiene el rol necesario.

La diferencia principal es:

- `401 Unauthorized`: falta el token o no es válido.
- `403 Forbidden`: el token es válido, pero el usuario no posee el rol exigido.

## Prueba automatizada de los permisos

Con MySQL, Keycloak y Spring Boot en ejecución, abre Git Bash y ejecuta:

```bash
./scripts/test-endpoints.sh
```

El script obtiene tokens para los tres usuarios y comprueba la matriz de
permisos. Requiere `curl` y `python3` disponibles desde Git Bash.
