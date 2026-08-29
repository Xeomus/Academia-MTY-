# PROYECTO 02 — JWT

## 9. Objetivo

Este proyecto cambia el mecanismo de autenticación.

La contraseña viaja solamente al hacer login:

```text
POST /api/auth/login
```

Después, las peticiones utilizan:

```http
Authorization: Bearer <token>
```

Puerto:

```text
8072
```

Endpoints principales:

```text
http://localhost:8072/api/auth/login
http://localhost:8072/api/employees
```

---

## 10. Arrancar `02-security-jwt`

Maven:

```powershell
.\mvnw spring-boot:run
```

Gradle:

```powershell
.\gradlew bootRun
```

---

## 11. Probar acceso sin token

```powershell
curl.exe -i http://localhost:8072/api/employees
```

Esperado:

```text
401 Unauthorized
```

---

## 12. Login incorrecto

```powershell
curl.exe -i -u john:MALA `
  -X POST http://localhost:8072/api/auth/login
```

Esperado:

```text
401 Unauthorized
```

---

## 13. Obtener JWT de John

```powershell
$login = curl.exe -s `
  -u john:test123 `
  -X POST http://localhost:8072/api/auth/login |
  ConvertFrom-Json

$TJ = $login.accessToken
```

Comprobar:

```powershell
$TJ
```

Debe comenzar normalmente con:

```text
eyJ...
```

---

## 14. John hace GET con JWT

```powershell
curl.exe -i `
  -H "Authorization: Bearer $TJ" `
  http://localhost:8072/api/employees
```

Esperado:

```text
200 OK
```

---

## 15. John intenta crear

```powershell
curl.exe -i `
  -H "Authorization: Bearer $TJ" `
  -X POST http://localhost:8072/api/employees `
  -H "Content-Type: application/json" `
  -d '{\"firstName\":\"X\",\"lastName\":\"Y\",\"email\":\"x@y.com\"}'
```

Esperado:

```text
403 Forbidden
```

---

## 16. HTTP Basic ya no funciona directamente contra `/employees`

```powershell
curl.exe -i `
  -u susan:test123 `
  http://localhost:8072/api/employees
```

Esperado:

```text
401 Unauthorized
```

El usuario y contraseña sirven para obtener un JWT en `/login`, pero `/api/employees` espera un `Bearer token`.

---

## 17. Obtener tokens de Mary y Susan

Mary:

```powershell
$TM = (curl.exe -s `
  -u mary:test123 `
  -X POST http://localhost:8072/api/auth/login |
  ConvertFrom-Json).accessToken
```

Susan:

```powershell
$TS = (curl.exe -s `
  -u susan:test123 `
  -X POST http://localhost:8072/api/auth/login |
  ConvertFrom-Json).accessToken
```

---

## 18. Mary crea un empleado temporal

```powershell
$nuevo = curl.exe -s `
  -H "Authorization: Bearer $TM" `
  -X POST http://localhost:8072/api/employees `
  -H "Content-Type: application/json" `
  -d '{\"firstName\":\"Temp\",\"lastName\":\"Jwt\",\"email\":\"temp@jwt.com\"}' |
  ConvertFrom-Json

$ID = $nuevo.id
```

Ver ID:

```powershell
$ID
```

---

## 19. Mary no puede borrar

```powershell
curl.exe -i `
  -H "Authorization: Bearer $TM" `
  -X DELETE "http://localhost:8072/api/employees/$ID"
```

Esperado:

```text
403 Forbidden
```

---

## 20. Susan sí puede borrar

```powershell
curl.exe -i `
  -H "Authorization: Bearer $TS" `
  -X DELETE "http://localhost:8072/api/employees/$ID"
```

Esperado:

```text
200 OK
```
## ¿Cómo funciona JWT?

`JWT` (JSON Web Token) es un mecanismo basado en tokens que permite que un usuario se autentique y posteriormente utilice un token para acceder a los recursos protegidos de la aplicación.

A diferencia de `HTTP Basic`, donde el usuario y la contraseña se envían en cada petición, con `JWT` las credenciales se utilizan principalmente durante el proceso de login.

En este proyecto, el flujo comienza realizando:

`POST /api/auth/login`

El usuario envía sus credenciales:

`john:test123`

Spring Security verifica que el usuario exista y que la contraseña sea correcta.

Si las credenciales son incorrectas:

`401 Unauthorized`

Si son correctas, el servidor genera un JWT y lo devuelve al cliente.

El flujo puede representarse así:
```text
Cliente
   |
   | username + password
   v
POST /api/auth/login
   |
   v
Spring Security
   |
   |-- Busca al usuario
   |-- Verifica la contraseña
   |-- Obtiene sus roles
   v
¿Credenciales válidas?
   |
   +-- NO --> 401 Unauthorized
   |
   +-- SÍ
        |
        v
   Genera un JWT
        |
        v
   Devuelve el token
```
