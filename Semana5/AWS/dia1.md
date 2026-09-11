# AWS día 1: EC2, S3 y RDS

Esta guía despliega `taskflow-api` en EC2, almacena el JAR en S3 y conecta la aplicación con PostgreSQL en RDS.

## Requisitos

- Cuenta de AWS activa y proyecto `taskflow-api` disponible localmente.
- Maven y SSH instalados.
- Región `us-east-1` o la región asignada a la cuenta.
- Gestor de contraseñas para credenciales y secretos.

## 1. Configurar un presupuesto

AWS Console → **Billing and Cost Management** → **Budgets** → **Create budget**.

| Campo | Valor |
| --- | --- |
| Setup | `Customize (advanced)` |
| Type | `Cost budget` |
| Name | `taskflow-5usd` |
| Period | `Monthly`, recurrente |
| Method | `Fixed` |
| Amount | `5.00 USD` |
| Alert 1 | `80 %`, gasto real, correo personal |
| Alert 2 | `100 %`, gasto previsto, correo personal |

No agregues acciones automáticas ni elimines el presupuesto al terminar.

**Validación:** el presupuesto muestra ambas alertas.

## 2. Proteger la cuenta y crear un usuario IAM

### 2.1 Activar MFA para root

AWS Console → nombre de la cuenta → **Security credentials** → **Multi-factor authentication** → **Assign MFA device**.

1. Asigna el nombre `mi-telefono`.
2. Selecciona **Authenticator app**.
3. Escanea el QR y captura dos códigos consecutivos.
4. Guarda la clave secreta del MFA en un gestor de contraseñas.

### 2.2 Crear el usuario administrativo

AWS Console → **IAM** → **Users** → **Create user**.

| Campo | Valor |
| --- | --- |
| User name | `taskflow-admin` |
| Console access | Habilitado |
| Password | Contraseña personalizada |
| Policy | `AdministratorAccess` |

Guarda la URL de acceso, el usuario y la contraseña fuera del repositorio. Cierra la sesión root e inicia sesión como `taskflow-admin`.

> En cuentas administradas sin usuario root, crea solo el usuario IAM según las opciones disponibles y conserva la región asignada.

**Validación:** la consola muestra `taskflow-admin` y la región correcta.

## 3. Crear la instancia EC2

AWS Console → **EC2** → **Instances** → **Launch instances**.

| Campo | Valor |
| --- | --- |
| Name | `taskflow-ec2` |
| AMI | Amazon Linux 2023, x86_64 |
| Instance type | `t3.micro` |
| Key pair | `taskflow-key`, RSA, `.pem` |
| Public IP | Habilitada |
| Security group | `taskflow-ec2-sg` |
| Storage | `8 GiB gp3` |

Guarda `taskflow-key.pem` fuera del repositorio. AWS no permite descargar otra vez la clave privada.

### Reglas de entrada

| Tipo | Puerto | Origen | Uso |
| --- | --- | --- | --- |
| SSH | `22` | `My IP` | Administración remota |
| Custom TCP | `8080` | `0.0.0.0/0` | Acceso público temporal a la API |

No abras SSH a `0.0.0.0/0`.

**Validación:** la instancia está `Running`, usa `taskflow-ec2-sg` y tiene una IPv4 pública.

## 4. Conectarse por SSH

Ejecuta desde la carpeta que contiene la llave:

```bash
chmod 400 taskflow-key.pem
ssh -i taskflow-key.pem ec2-user@<IP_PUBLICA>
```

| Comando | Función |
| --- | --- |
| `chmod 400` | Limita la lectura de la clave al propietario. |
| `ssh -i` | Abre una sesión en EC2 con la clave indicada. |

Si PowerShell rechaza los permisos de la llave:

```powershell
icacls taskflow-key.pem /inheritance:r /grant:r "$env:USERNAME:R"
ssh -i taskflow-key.pem ec2-user@<IP_PUBLICA>
```

También se puede usar **EC2 Instance Connect** desde **Connect**.

Dentro de EC2:

```bash
uname -a
curl ifconfig.me
free -m
```

| Comando | Función |
| --- | --- |
| `uname -a` | Muestra información del sistema. |
| `curl ifconfig.me` | Consulta la IP pública. |
| `free -m` | Muestra la memoria disponible en MB. |

**Validación:** SSH abre con el usuario `ec2-user`.

## 5. Instalar Java 21

```bash
sudo dnf install -y java-21-amazon-corretto-headless
java -version
```

| Comando | Función |
| --- | --- |
| `dnf install` | Instala Amazon Corretto 21. |
| `java -version` | Comprueba la versión activa. |

**Validación:** la salida indica Java 21.

## 6. Compilar y transferir la aplicación

Ejecuta localmente:

```bash
cd <RUTA_PROYECTO>/taskflow-api
mvn -q -DskipTests package
ls -lh target/*.jar
scp -i <RUTA_LLAVE>/taskflow-key.pem target/taskflow-api-*.jar ec2-user@<IP_PUBLICA>:~/taskflow-api.jar
```

| Comando | Función |
| --- | --- |
| `cd` | Entra al proyecto. |
| `mvn package` | Compila y genera el JAR sin ejecutar pruebas. |
| `ls -lh` | Confirma el artefacto generado. |
| `scp` | Copia el JAR a EC2. |

No compiles dentro de una instancia `t3.micro`; su memoria es limitada.

### Verificar integridad

En macOS, Linux o Git Bash:

```bash
shasum -a 256 target/taskflow-api-*.jar
```

En PowerShell:

```powershell
Get-FileHash -Algorithm SHA256 target\taskflow-api-3.0.0.jar
```

En EC2:

```bash
sha256sum ~/taskflow-api.jar
```

Estos comandos calculan el hash SHA-256 del archivo local y del transferido.

**Validación:** ambos hashes son idénticos.

## 7. Iniciar la API

```bash
nohup java -jar taskflow-api.jar > app.log 2>&1 &
tail -f app.log
```

| Comando | Función |
| --- | --- |
| `nohup java -jar ... &` | Inicia la API en segundo plano y escribe en `app.log`. |
| `tail -f` | Muestra el log en tiempo real. |

Abre `http://<IP_PUBLICA>:8080/swagger-ui/index.html`.

Si no responde, ejecuta en orden:

```bash
ps aux | grep java
curl -s localhost:8080/swagger-ui/index.html | head -3
```

| Resultado | Acción |
| --- | --- |
| No aparece Java | Revisar `app.log`. |
| Responde en `localhost`, pero no desde internet | Revisar la regla TCP `8080`. |
| La URL usa HTTPS | Cambiarla a HTTP. |

**Validación:** Swagger abre desde un equipo externo.

## 8. Crear PostgreSQL en RDS

AWS Console → **RDS** → **Databases** → **Create database** → **Full configuration**.

| Campo | Valor |
| --- | --- |
| Engine | PostgreSQL |
| Template | Free tier o Dev/Test |
| Deployment | Single-AZ |
| Identifier | `taskflow-db` |
| Master username | `taskflow` |
| Credentials | Self managed |
| Instance class | `db.t4g.micro` o `db.t3.micro` |
| Storage | `20 GiB`, sin autoscaling |
| VPC | Default |
| Public access | `No` |
| Security group | `taskflow-rds-sg` |
| Authentication | Password |
| Initial database | `taskflow` |
| Automated backups | Deshabilitados para la práctica |
| Deletion protection | Deshabilitada para la práctica |

Guarda la contraseña fuera del repositorio. Cuando RDS esté `Available`, copia el endpoint desde **Connectivity & security**. El puerto es `5432`.

**Validación:** RDS está disponible y se registró su endpoint.

## 9. Almacenar el JAR en S3

AWS Console → **S3** → **Create bucket**.

| Campo | Valor |
| --- | --- |
| Bucket type | General purpose |
| Name | `taskflow-artefactos-<USUARIO_GITHUB>` |
| Block Public Access | Habilitado |

El nombre debe estar en minúsculas, sin espacios ni guiones bajos, y ser único globalmente.

1. Abre el bucket y selecciona **Upload** → **Add files**.
2. Sube `target/taskflow-api-3.0.0.jar`.
3. Abre la URL directa del objeto; debe responder `AccessDenied`.
4. Selecciona **Actions** → **Share with a presigned URL**.
5. Configura cinco minutos de vigencia y abre la URL generada.

**Validación:** la URL directa está bloqueada y la URL prefirmada descarga el JAR.

## 10. Conectar la API con RDS

### 10.1 Generar el secreto JWT

```bash
openssl rand -hex 32
```

Genera un secreto aleatorio de 256 bits. Guárdalo fuera del repositorio y no uses el secreto de desarrollo.

### 10.2 Configurar la red

AWS Console → **EC2** → **Security Groups** → `taskflow-rds-sg` → **Inbound rules** → **Edit inbound rules**.

| Tipo | Puerto | Origen |
| --- | --- | --- |
| PostgreSQL | `5432` | Security group de EC2 (`taskflow-ec2-sg`) |

El origen debe ser el ID del security group de EC2, no una IP pública ni el grupo de RDS.

### 10.3 Detener la API anterior

```bash
ps aux | grep java
kill <PID>
```

| Comando | Función |
| --- | --- |
| `ps aux | grep java` | Localiza el proceso y su PID. |
| `kill` | Libera el puerto `8080`. |

### 10.4 Iniciar con PostgreSQL

```bash
nohup java -jar taskflow-api.jar \
  --spring.profiles.active=docker \
  --DB_HOST=<ENDPOINT_RDS> \
  --DB_PORT=5432 \
  --DB_NAME=taskflow \
  --DB_USER=taskflow \
  --DB_PASSWORD='<CONTRASENA_RDS>' \
  --JWT_SECRET='<SECRETO_JWT>' \
  > app.log 2>&1 &
```

| Parámetro | Función |
| --- | --- |
| `spring.profiles.active` | Activa la configuración externa. |
| `DB_HOST` y `DB_PORT` | Definen el endpoint y puerto de RDS. |
| `DB_NAME` y `DB_USER` | Seleccionan la base y el usuario. |
| `DB_PASSWORD` | Proporciona la contraseña de RDS. |
| `JWT_SECRET` | Firma y valida tokens JWT. |

Revisa errores con:

```bash
grep -n "Caused by" app.log | head -3
```

- `Connect timed out`: revisar la regla `5432` desde el security group de EC2.
- `Port 8080 was already in use`: detener el proceso anterior.

**Validación:** Swagger y el inicio de sesión funcionan contra la base `taskflow` de RDS.

## Resultado

- API ejecutándose en EC2.
- JAR privado almacenado en S3.
- PostgreSQL privado en RDS.
- Acceso a RDS limitado al security group de EC2.
- Secretos y llaves fuera del repositorio.
