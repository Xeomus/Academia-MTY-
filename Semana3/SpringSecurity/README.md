# Guía de ejecución en Windows PowerShell — Seguridad con Spring Boot

Esta guía explica cómo preparar y ejecutar los tres proyectos de seguridad en Windows usando PowerShell:

1. `01-security-basic` — HTTP Basic + roles
2. `02-security-jwt` — JWT + roles
3. `03-security-oauth2` — OAuth2 Resource Server + Keycloak

También incluye la preparación de MySQL y Keycloak con Docker.

---

# 0. Requisitos

Antes de comenzar asegúrate de tener instalado:

- Java 21 o superior
- Docker Desktop
- PowerShell
- Maven Wrapper o Gradle Wrapper incluido en cada proyecto
- DBeaver para administrar MySQL
- Git Bash solo si deseas ejecutar scripts `.sh`

Comprobar Java:

```powershell
java -version
```

Comprobar Docker:

```powershell
docker --version
docker ps
```

---

# 1. MySQL con Docker

Los proyectos usan una base de datos MySQL.

## 1.1 Crear el contenedor

Ejecutar una sola vez:

```powershell
docker run --name mysql-9.7 `
  -e MYSQL_ROOT_PASSWORD=admin `
  -p 3306:3306 `
  -d mysql:9.7
```

Si aparece:

```text
Conflict. The container name "/mysql-9.7" is already in use
```

el contenedor ya existe.

Comprueba su estado:

```powershell
docker ps -a
```

Si está detenido:

```powershell
docker start mysql-9.7
```

Comprobar que está activo:

```powershell
docker ps
```

Debe aparecer el puerto:

```text
0.0.0.0:3306->3306/tcp
```

---

# 2. Conectarse a MySQL desde DBeaver

Crear una conexión MySQL con:

```text
Host: localhost
Port: 3306
Usuario: root
Password: admin
```

---

# 3. Crear usuario y base de datos

En DBeaver abre un SQL Editor conectado como `root`.

## 3.1 Crear usuario para Spring Boot

```sql
DROP USER IF EXISTS 'springstudent'@'%';

CREATE USER 'springstudent'@'%'
IDENTIFIED BY 'springstudent';

GRANT ALL PRIVILEGES ON *.* TO 'springstudent'@'%';

FLUSH PRIVILEGES;
```

## 3.2 Crear base de datos

```sql
CREATE DATABASE IF NOT EXISTS employee_directory;

USE employee_directory;
```

## 3.3 Crear tabla employee

> Cuidado: `DROP TABLE` elimina la tabla y todos sus datos.

```sql
DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
    id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(45) DEFAULT NULL,
    last_name VARCHAR(45) DEFAULT NULL,
    email VARCHAR(45) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB
AUTO_INCREMENT=1
DEFAULT CHARSET=latin1;
```

Comprobar:

```sql
USE employee_directory;

SHOW TABLES;

SELECT * FROM employee;
```

---

# 4. Configuración de Spring Boot

Verifica el archivo:

```text
src/main/resources/application.properties
```

La conexión a MySQL debe ser equivalente a:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_directory
spring.datasource.username=springstudent
spring.datasource.password=springstudent
```

---

# 5. Mapa de puertos

| Puerto | Servicio                              |
| ------ | ------------------------------------- |
| `3306` | MySQL                                 |
| `8070` | Proyecto REST con MySQL sin seguridad |
| `8071` | `01-security-basic`                   |
| `8072` | `02-security-jwt`                     |
| `8073` | `03-security-oauth2`                  |
| `8081` | Proyecto MongoDB                      |
| `8090` | Keycloak                              |

---

# 34. Diferencia entre los tres proyectos

## Proyecto 01 — HTTP Basic

Cada petición contiene:

```http
Authorization: Basic ...
```

Conceptualmente:

```text
usuario + password
        |
        v
Spring Security
        |
        v
API
```

La contraseña participa en cada petición.

---

## Proyecto 02 — JWT propio

Primero:

```text
usuario + password
        |
        v
/login
        |
        v
JWT
```

Después:

```text
JWT
 |
 v
API
```

La contraseña solamente se usa para obtener el token.

---

## Proyecto 03 — OAuth2 + Keycloak

Keycloak se encarga de autenticar:

```text
usuario + password
        |
        v
Keycloak
        |
        v
Access Token
        |
        v
Spring Boot Resource Server
```

La API confía en los tokens emitidos por Keycloak.

---

# 35. Diferencia entre 401 y 403

## `401 Unauthorized`

En la práctica significa que falló la autenticación.

Ejemplos:

- no enviaste credenciales
- password incorrecta
- no enviaste JWT
- JWT inválido
- JWT mal formado
- JWT expirado

Ejemplo:

```text
No sé quién eres.
```

---

## `403 Forbidden`

La autenticación fue correcta, pero el usuario no tiene autorización suficiente.

Ejemplo:

```text
Sé quién eres, pero tu rol no permite esta operación.
```

Caso:

```text
john + POST -> 403
```

porque John está autenticado, pero solamente tiene rol de empleado.

---

# 36. Comandos útiles de Docker

Ver contenedores activos:

```powershell
docker ps
```

Ver todos:

```powershell
docker ps -a
```

Arrancar MySQL:

```powershell
docker start mysql-9.7
```

Arrancar Keycloak:

```powershell
docker start keycloak-academy
```

Detener MySQL:

```powershell
docker stop mysql-9.7
```

Detener Keycloak:

```powershell
docker stop keycloak-academy
```

Logs de Keycloak:

```powershell
docker logs keycloak-academy
```

Ver imágenes:

```powershell
docker images
```

---

# 37. Orden recomendado después de reiniciar Windows

## Paso 1 — Docker Desktop

Asegúrate de que Docker Desktop esté iniciado.

## Paso 2 — MySQL

```powershell
docker start mysql-9.7
```

## Paso 3 — Keycloak

Solo necesario para el proyecto 03:

```powershell
docker start keycloak-academy
```

## Paso 4 — comprobar contenedores

```powershell
docker ps
```

## Paso 5 — arrancar el proyecto Spring Boot correspondiente

Proyecto 01:

```text
8071
```

Proyecto 02:

```text
8072
```

Proyecto 03:

```text
8073
```

## Paso 6 — ejecutar pruebas

Usa los comandos de cada sección.

---

# 38. Resumen rápido

```text
01-security-basic
Usuario/password -> Spring Security -> API
Puerto 8071

02-security-jwt
Usuario/password -> /login -> JWT -> API
Puerto 8072

03-security-oauth2
Usuario/password -> Keycloak :8090 -> JWT -> API :8073
```

Servicios externos:

```text
MySQL    localhost:3306
Keycloak localhost:8090
```

Base de datos:

```text
employee_directory
```

Usuario MySQL para Spring:

```text
springstudent / springstudent
```

Administrador MySQL:

```text
root / admin
```

Administrador Keycloak:

```text
admin / admin
```
