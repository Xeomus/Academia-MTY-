# PROYECTO 03 — OAuth2 + Keycloak

## 21. Objetivo

En este proyecto Spring Boot ya no administra directamente el login.

Keycloak funciona como servidor de identidad.

Arquitectura:

```text
Usuario
   |
   | usuario + password
   v
Keycloak :8090
   |
   | Access Token JWT
   v
Spring Boot Resource Server :8073
   |
   v
/api/employees
```

---

# 22. Crear Keycloak con Docker

Ejecutar una sola vez:

```powershell
docker run --name keycloak-academy -p 8090:8080 `
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin `
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin `
  -d quay.io/keycloak/keycloak:26.4 start-dev
```

Si la descarga de la imagen falla temporalmente:

```powershell
docker pull quay.io/keycloak/keycloak:26.4
```

Después vuelve a ejecutar `docker run`.

---

## 23. Arrancar Keycloak posteriormente

Las siguientes veces no debes volver a crear el contenedor:

```powershell
docker start keycloak-academy
```

Comprobar:

```powershell
docker ps
```

Logs:

```powershell
docker logs keycloak-academy
```

---

## 24. Comprobar Keycloak

```powershell
curl.exe http://localhost:8090/realms/master/.well-known/openid-configuration
```

Si devuelve JSON, Keycloak está funcionando.

Consola administrativa:

```text
http://localhost:8090
```

Credenciales:

```text
Usuario: admin
Password: admin
```

---

# 25. Configurar realm `academy`

El proyecto incluye:

```text
scripts/keycloak-setup.sh
```

Este script crea:

- realm `academy`
- client `employee-api`
- roles
- usuarios
- asignación de roles

---

## 26. Ejecutar `keycloak-setup.sh` desde Windows

No conviene ejecutar directamente el `.sh` desde PowerShell.

Abre Git Bash dentro de:

```text
03-security-oauth2
```

Debido a que Git Bash intenta convertir rutas Linux como:

```text
/opt/keycloak/bin/kcadm.sh
```

a rutas Windows, ejecuta:

```bash
MSYS_NO_PATHCONV=1 ./scripts/keycloak-setup.sh
```

Si hiciera falta, alternativa:

```bash
export MSYS2_ARG_CONV_EXCL="*"
./scripts/keycloak-setup.sh
```

---

## 27. Comprobar realm `academy`

Desde PowerShell:

```powershell
curl.exe http://localhost:8090/realms/academy/.well-known/openid-configuration
```

Debe aparecer:

```json
"issuer":"http://localhost:8090/realms/academy"
```

Si aparece:

```json
{ "error": "Realm does not exist" }
```

el script de configuración todavía no se ejecutó correctamente.

---

# 28. Arrancar `03-security-oauth2`

Desde la carpeta del proyecto:

Maven:

```powershell
.\mvnw spring-boot:run
```

Gradle:

```powershell
.\gradlew bootRun
```

La API debe arrancar en:

```text
http://localhost:8073
```

---

# 29. Probar la API sin token

```powershell
curl.exe -i http://localhost:8073/api/employees
```

Esperado:

```text
401 Unauthorized
```

Esto confirma que el Resource Server está protegido.

---

# 30. Obtener token desde Keycloak

Ejemplo con `john`:

```powershell
$tokenResponse = curl.exe -s `
  -X POST "http://localhost:8090/realms/academy/protocol/openid-connect/token" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "client_id=employee-api" `
  -d "username=john" `
  -d "password=test123" `
  -d "grant_type=password" |
  ConvertFrom-Json
```

Guardar el access token:

```powershell
$TOKEN = $tokenResponse.access_token
```

Comprobar:

```powershell
$TOKEN
```

Debe ser un JWT y normalmente comenzar con:

```text
eyJ...
```

---

# 31. Usar el token contra Spring Boot

```powershell
curl.exe -i `
  -H "Authorization: Bearer $TOKEN" `
  http://localhost:8073/api/employees
```

Si el usuario tiene permisos de lectura:

```text
200 OK
```

---

# 32. Ver Keycloak desde el navegador

Abrir:

```text
http://localhost:8090
```

Entrar a la consola administrativa:

```text
admin
admin
```

Selecciona el realm:

```text
academy
```

Desde ahí puedes revisar:

```text
Clients
  └── employee-api

Realm roles

Users
  ├── john
  ├── mary
  └── susan
```

---

# 33. Ver `/api/employees` desde navegador

Puedes abrir:

```text
http://localhost:8073/api/employees
```

pero normalmente obtendrás `401` porque el navegador no envía automáticamente:

```http
Authorization: Bearer <JWT>
```

Para pruebas de Resource Server es más cómodo usar:

- PowerShell + `curl.exe`
- Postman
- Insomnia

---
