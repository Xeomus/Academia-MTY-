# PROYECTO 01 — HTTP Basic

## 6. Objetivo

Este proyecto utiliza HTTP Basic con roles.

Usuarios:

| Usuario | Password  | Roles                                         |
| ------- | --------- | --------------------------------------------- |
| `john`  | `test123` | `ROLE_EMPLOYEE`                               |
| `mary`  | `test123` | `ROLE_EMPLOYEE`, `ROLE_MANAGER`               |
| `susan` | `test123` | `ROLE_EMPLOYEE`, `ROLE_MANAGER`, `ROLE_ADMIN` |

Permisos esperados:

| Usuario | GET |      POST |       PUT |    DELETE |
| ------- | --: | --------: | --------: | --------: |
| john    | 200 |       403 |       403 |       403 |
| mary    | 200 | permitido | permitido |       403 |
| susan   | 200 | permitido | permitido | permitido |

Puerto:

```text
8071
```

---

## 7. Arrancar `01-security-basic`

Desde la carpeta del proyecto.

Maven:

```powershell
.\mvnw spring-boot:run
```

Gradle:

```powershell
.\gradlew bootRun
```

---

## 8. Probar HTTP Basic desde PowerShell

### Sin credenciales

```powershell
curl.exe -i http://localhost:8071/api/employees
```

Esperado:

```text
401 Unauthorized
```

### Credenciales incorrectas

```powershell
curl.exe -i -u john:MALA http://localhost:8071/api/employees
```

Esperado:

```text
401 Unauthorized
```

### John puede leer

```powershell
curl.exe -i -u john:test123 http://localhost:8071/api/employees
```

Esperado:

```text
200 OK
```

### John no puede crear

```powershell
curl.exe -i -u john:test123 `
  -X POST http://localhost:8071/api/employees `
  -H "Content-Type: application/json" `
  -d '{\"firstName\":\"X\",\"lastName\":\"Y\",\"email\":\"x@y.com\"}'
```

Esperado:

```text
403 Forbidden
```

### Mary puede crear

```powershell
curl.exe -i -u mary:test123 `
  -X POST http://localhost:8071/api/employees `
  -H "Content-Type: application/json" `
  -d '{\"firstName\":\"Temp\",\"lastName\":\"Basic\",\"email\":\"temp@basic.com\"}'
```

Guarda el ID que regrese.

### Mary no puede borrar

```powershell
curl.exe -i -u mary:test123 `
  -X DELETE http://localhost:8071/api/employees/ID
```

Esperado:

```text
403 Forbidden
```

### Susan puede borrar

```powershell
curl.exe -i -u susan:test123 `
  -X DELETE http://localhost:8071/api/employees/ID
```

Esperado:

```text
200 OK
```

## ¿Cómo funciona HTTP Basic?

`HTTP Basic` es un mecanismo de autenticación en el que el cliente envía sus credenciales en cada petición `HTTP` que realiza al servidor.

Las credenciales se envían mediante el header Authorization utilizando el siguiente formato:

`Authorization: Basic <credenciales>`

Para generar las credenciales, el usuario y la contraseña se combinan utilizando el formato:

`username:password`

Por ejemplo:

`john:test123`

Esta cadena se codifica utilizando Base64 y se envía dentro del header Authorization.

Cuando ejecutamos:

```powershell
curl.exe -u john:test123 http://localhost:8071/api/employees
```

curl genera automáticamente el header Authorization correspondiente y lo incluye en la petición.

El flujo de autenticación puede representarse de la siguiente manera:
```text
Cliente
   |
   | GET /api/employees
   | Authorization: Basic <credenciales>
   v
Servidor / Spring Security
   |
   |-- Decodifica las credenciales
   |-- Busca al usuario
   |-- Verifica la contraseña
   |-- Obtiene sus roles
   v
¿Credenciales válidas?
   |
   +-- NO --> 401 Unauthorized
   |
   +-- SÍ --> Comprueba los permisos
                 |
                 +-- Sin permiso --> 403 Forbidden
                 |
                 +-- Con permiso --> Ejecuta la petición
```
