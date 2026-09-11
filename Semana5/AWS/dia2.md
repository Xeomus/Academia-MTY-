# AWS día 2: DynamoDB, CodePipeline y CodeDeploy

Esta guía crea una tabla en DynamoDB y configura un pipeline que compila y despliega `taskflow-api` automáticamente en EC2.

## Requisitos

- Cuenta con acceso a IAM, EC2, S3, DynamoDB, CodeBuild, CodeDeploy y CodePipeline.
- AWS CLI, Git y Java disponibles.
- Cuenta de GitHub y proyecto `taskflow-api` local.
- Región `us-east-1` o la asignada a la cuenta.

## 1. Preparar el repositorio

Crea en GitHub un repositorio vacío llamado `taskflow-aws-<USUARIO_GITHUB>`.

```bash
cp -R <RUTA_PROYECTO>/taskflow-api ~/taskflow-aws-<USUARIO_GITHUB>
cd ~/taskflow-aws-<USUARIO_GITHUB>
rm -rf target data .git
git init
git add -A
git commit -m "TaskFlow API, punto de partida"
git branch -M main
git remote add origin https://github.com/<USUARIO_GITHUB>/taskflow-aws-<USUARIO_GITHUB>.git
git push -u origin main
```

| Comando | Función |
| --- | --- |
| `cp -R` | Crea una copia independiente del proyecto. |
| `rm -rf target data .git` | Elimina compilados, datos locales e historial de la copia. |
| `git init` | Inicializa el repositorio. |
| `git add` y `git commit` | Registra el estado inicial. |
| `git branch -M main` | Define `main` como rama principal. |
| `git remote add` | Asocia el repositorio con GitHub. |
| `git push` | Publica la rama. |

**Validación:** GitHub contiene el código sin `target/` ni `data/`.

## 2. Conectar AWS con GitHub

AWS Console → **CodePipeline** → **Settings** → **Connections** → **Create connection**.

1. Selecciona **GitHub**.
2. Usa el nombre `github-taskflow`.
3. Selecciona **Connect to GitHub**.
4. Instala **AWS Connector for GitHub** solo en el repositorio del proyecto.
5. Selecciona **Connect**.

La conexión debe estar en la misma región que CodePipeline.

**Validación:** la conexión aparece como `Available`.

## 3. Crear el rol de EC2

AWS Console → **IAM** → **Roles** → **Create role**.

| Campo | Valor |
| --- | --- |
| Trusted entity | AWS service |
| Use case | EC2 |
| Policy | `AmazonS3ReadOnlyAccess` |
| Role name | `taskflow-ec2-role` |

El rol permite descargar artefactos desde S3.

## 4. Crear la instancia EC2

AWS Console → **EC2** → **Instances** → **Launch instances**.

| Campo | Valor |
| --- | --- |
| Name tag | `taskflow-ec2` |
| AMI | Amazon Linux 2023, x86_64 |
| Instance type | `t3.micro` |
| Key pair | `taskflow-key`, RSA, `.pem` |
| Security group | `taskflow-ec2-sg` |
| SSH | Puerto `22`, origen `My IP` |
| API | Puerto `8080`, origen `0.0.0.0/0` |
| IAM instance profile | `taskflow-ec2-role` |

CodeDeploy seleccionará la instancia mediante el tag `Name=taskflow-ec2`.

```bash
chmod 400 <RUTA_LLAVE>/taskflow-key.pem
ssh -i <RUTA_LLAVE>/taskflow-key.pem ec2-user@<IP_PUBLICA>
sudo dnf install -y java-21-amazon-corretto-headless
java -version
```

| Comando | Función |
| --- | --- |
| `chmod 400` | Restringe la llave privada. |
| `ssh` | Abre una sesión en EC2. |
| `dnf install` | Instala Amazon Corretto 21. |
| `java -version` | Comprueba la instalación. |

No copies el JAR manualmente; lo instalará el pipeline.

**Validación:** la instancia tiene el rol, el tag y Java 21.

## 5. Configurar AWS CLI

Si `aws --version` falla, instala AWS CLI v2. Crea una access key desde **IAM** → **Users** → usuario administrativo → **Security credentials** → **Access keys**.

```bash
aws configure
aws configure set cli_pager ""
aws sts get-caller-identity
```

| Entrada o comando | Función |
| --- | --- |
| Access Key ID y Secret Access Key | Autentican la CLI. |
| Default region | Define la región de los recursos. |
| Output `json` | Configura el formato de respuesta. |
| `cli_pager ""` | Deshabilita el paginador. |
| `sts get-caller-identity` | Muestra la identidad IAM activa. |

No publiques las credenciales ni las guardes en el repositorio.

**Validación:** STS devuelve la cuenta y el ARN esperados.

## 6. Crear la tabla DynamoDB

| Clave | Atributo | Función |
| --- | --- | --- |
| Partition key | `taskId` | Agrupa los eventos por tarea. |
| Sort key | `fechaHora` | Ordena los eventos de cada tarea. |

```bash
aws dynamodb create-table \
  --table-name taskflow-eventos \
  --attribute-definitions AttributeName=taskId,AttributeType=S AttributeName=fechaHora,AttributeType=S \
  --key-schema AttributeName=taskId,KeyType=HASH AttributeName=fechaHora,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST

aws dynamodb describe-table \
  --table-name taskflow-eventos \
  --query "Table.TableStatus"
```

| Parámetro o comando | Función |
| --- | --- |
| `--attribute-definitions` | Declara los atributos de clave. |
| `KeyType=HASH` | Define la partition key. |
| `KeyType=RANGE` | Define la sort key. |
| `PAY_PER_REQUEST` | Usa capacidad bajo demanda. |
| `describe-table` | Consulta el estado de la tabla. |

**Validación:** el estado es `"ACTIVE"`.

## 7. Insertar eventos

Ejecuta en orden:

```bash
aws dynamodb put-item --table-name taskflow-eventos --item '{"taskId":{"S":"T-001"},"fechaHora":{"S":"2026-09-08T09:15:00Z"},"tipo":{"S":"CREADA"},"autor":{"S":"ana"},"detalle":{"S":"Maquetar la pantalla de proyectos"}}'

aws dynamodb put-item --table-name taskflow-eventos --item '{"taskId":{"S":"T-001"},"fechaHora":{"S":"2026-09-08T10:02:00Z"},"tipo":{"S":"ASIGNADA"},"autor":{"S":"admin"},"detalle":{"S":"asignada a luis"}}'

aws dynamodb put-item --table-name taskflow-eventos --item '{"taskId":{"S":"T-001"},"fechaHora":{"S":"2026-09-08T11:40:00Z"},"tipo":{"S":"EN_PROGRESO"},"autor":{"S":"luis"}}'

aws dynamodb put-item --table-name taskflow-eventos --item '{"taskId":{"S":"T-002"},"fechaHora":{"S":"2026-09-08T09:30:00Z"},"tipo":{"S":"CREADA"},"autor":{"S":"luis"},"detalle":{"S":"Revisar el contrato de la API"}}'

aws dynamodb put-item --table-name taskflow-eventos --item '{"taskId":{"S":"T-002"},"fechaHora":{"S":"2026-09-08T16:05:00Z"},"tipo":{"S":"COMPLETADA"},"autor":{"S":"luis"}}'

aws dynamodb scan --table-name taskflow-eventos --select COUNT
```

| Comando | Función |
| --- | --- |
| `put-item` | Inserta o reemplaza un elemento con la misma clave. |
| `scan --select COUNT` | Cuenta los elementos leídos. |

**Validación:** el conteo devuelve `5`.

## 8. Comparar Query y Scan

Consulta una partición:

```bash
aws dynamodb query \
  --table-name taskflow-eventos \
  --key-condition-expression "taskId = :id" \
  --expression-attribute-values '{":id":{"S":"T-001"}}' \
  --return-consumed-capacity TOTAL
```

Recorre toda la tabla y filtra:

```bash
aws dynamodb scan \
  --table-name taskflow-eventos \
  --filter-expression "#tipo = :tipo" \
  --expression-attribute-names '{"#tipo":"tipo"}' \
  --expression-attribute-values '{":tipo":{"S":"COMPLETADA"}}' \
  --return-consumed-capacity TOTAL
```

| Operación | Función |
| --- | --- |
| `query` | Lee una partition key concreta. |
| `scan` | Lee toda la tabla antes de filtrar. |
| `--return-consumed-capacity` | Muestra la capacidad consumida. |

Compara `Count`, `ScannedCount` y `ConsumedCapacity`. Usa `query` para patrones de acceso conocidos.

## 9. Crear el bucket de artefactos

AWS Console → **S3** → **Create bucket**.

| Campo | Valor |
| --- | --- |
| Type | General purpose |
| Name | `taskflow-artefactos-<USUARIO_GITHUB>` |
| Block Public Access | Habilitado |
| Versioning | Habilitado |

El pipeline almacenará aquí sus artefactos versionados.

## 10. Instalar el agente de CodeDeploy

Ejecuta dentro de EC2:

```bash
sudo dnf install -y ruby wget
cd /home/ec2-user
REGION=us-east-1
wget https://aws-codedeploy-$REGION.s3.$REGION.amazonaws.com/latest/install
head -1 install
chmod +x install
sudo ./install auto
sudo systemctl status codedeploy-agent
```

| Comando | Función |
| --- | --- |
| `dnf install` | Instala las dependencias. |
| `REGION=...` | Define la región del instalador. |
| `wget` | Descarga el instalador oficial. |
| `head -1` | Verifica que sea un script Ruby. |
| `chmod +x` | Agrega permiso de ejecución. |
| `./install auto` | Instala el agente. |
| `systemctl status` | Consulta el servicio. |

Cambia `REGION` si utilizas otra región.

**Validación:** `codedeploy-agent` está `active (running)`.

## 11. Preparar los archivos de despliegue

| Archivo | Consumidor | Función |
| --- | --- | --- |
| `buildspec.yml` | CodeBuild | Compila y define el artefacto. |
| `appspec.yml` | CodeDeploy | Define destinos y hooks. |
| `taskflow.service` | systemd | Ejecuta la API como servicio. |
| `scripts/*.sh` | CodeDeploy | Detiene, instala, inicia y valida. |

```bash
cd ~/taskflow-aws-<USUARIO_GITHUB>
cp <RUTA_MATERIALES>/pipeline/taskflow.service .
cp <RUTA_MATERIALES>/pipeline/buildspec.yml .
cp <RUTA_MATERIALES>/pipeline/appspec.yml .
cp -R <RUTA_MATERIALES>/pipeline/scripts .
printf 'target/\ndata/\n' > .gitignore
ls -a
```

| Comando | Función |
| --- | --- |
| `cp` | Copia los descriptores del pipeline. |
| `cp -R` | Copia los hooks. |
| `printf` | Excluye compilados y datos locales. |
| `ls -a` | Comprueba la estructura. |

### Servicio systemd

`taskflow.service` debe contener:

```ini
[Unit]
Description=TaskFlow API
After=network.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/opt/taskflow
ExecStart=/usr/bin/java -jar /opt/taskflow/taskflow-api.jar
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

| Directiva | Función |
| --- | --- |
| `User` | Evita ejecutar como root. |
| `WorkingDirectory` | Define la ruta de trabajo. |
| `ExecStart` | Inicia el JAR. |
| `SuccessExitStatus=143` | Acepta la terminación enviada por systemd. |
| `Restart=always` | Reinicia el proceso si se detiene. |

### Buildspec

Comprueba en `buildspec.yml`:

- Runtime `corretto21`.
- Ejecución de `mvn package`.
- Renombrado a `target/taskflow-api.jar`.
- Inclusión de `appspec.yml`, `taskflow.service` y `scripts/**/*`.

### Hooks

| Hook | Script | Función |
| --- | --- | --- |
| `ApplicationStop` | `scripts/parar.sh` | Detiene la versión activa. |
| `AfterInstall` | `scripts/permisos.sh` | Ajusta permisos. |
| `ApplicationStart` | `scripts/arrancar.sh` | Inicia el servicio. |
| `ValidateService` | `scripts/verificar.sh` | Confirma que la API responde. |

```bash
git add scripts
git update-index --chmod=+x scripts/*.sh
git ls-files -s scripts/
git add .gitignore buildspec.yml appspec.yml taskflow.service scripts
git commit -m "ci: buildspec, appspec, hooks y taskflow.service"
git push
```

| Comando | Función |
| --- | --- |
| `git update-index --chmod=+x` | Registra los scripts como ejecutables. |
| `git ls-files -s` | Comprueba el modo `100755`. |
| `git commit` y `git push` | Publican la configuración. |

## 12. Configurar CodeDeploy

### 12.1 Rol de servicio

AWS Console → **IAM** → **Roles** → **Create role**.

| Campo | Valor |
| --- | --- |
| Use case | CodeDeploy |
| Policy | `AWSCodeDeployRole` |
| Role name | `taskflow-codedeploy-role` |

### 12.2 Aplicación y grupo

AWS Console → **CodeDeploy** → **Applications** → **Create application**.

| Campo | Valor |
| --- | --- |
| Application | `taskflow` |
| Platform | EC2/On-premises |
| Deployment group | `taskflow-dg` |
| Service role | `taskflow-codedeploy-role` |
| Deployment type | In-place |
| Environment | Tag `Name=taskflow-ec2` |
| Install agent | Never |

Selecciona **Never** porque el agente ya está instalado.

## 13. Crear CodePipeline

AWS Console → **CodePipeline** → **Create pipeline**.

| Etapa | Configuración |
| --- | --- |
| Pipeline | `taskflow-pipeline` |
| Source | GitHub, conexión `github-taskflow`, rama `main` |
| Build provider | AWS CodeBuild |
| Build project | `taskflow-build` |
| Environment | Amazon Linux, Standard, `amazonlinux-x86_64-standard` |
| Instructions | Use a buildspec file |
| Deploy provider | AWS CodeDeploy |
| Application | `taskflow` |
| Deployment group | `taskflow-dg` |

Los asistentes crean los roles de CodeBuild y CodePipeline. Si CodeBuild no puede escribir en S3, agrega a su rol permisos limitados al bucket de artefactos y reintenta la etapa.

### Origen alternativo en S3

```bash
git -c core.autocrlf=false archive --format=zip -o /tmp/taskflow-src.zip HEAD
aws s3 cp /tmp/taskflow-src.zip s3://taskflow-artefactos-<USUARIO_GITHUB>/taskflow-src.zip
```

| Comando | Función |
| --- | --- |
| `git archive` | Empaqueta la revisión actual sin metadatos de Git. |
| `aws s3 cp` | Sube el código al bucket. |

Configura el objeto como etapa **Source** si no utilizas GitHub.

## 14. Verificar el despliegue automático

Cambia la versión en `InfoController.java`:

```java
public static final String VERSION = "3.0.1";
```

```bash
git add .
git commit -m "chore: v3.0.1"
git push
```

| Comando | Función |
| --- | --- |
| `git add` | Prepara el cambio. |
| `git commit` | Crea una revisión. |
| `git push` | Activa el pipeline. |

Al finalizar Source, Build y Deploy, abre `http://<IP_PUBLICA>:8080/info`.

Respuesta esperada:

```json
{"version":"3.0.1","app":"taskflow-api"}
```

**Validación:** el endpoint muestra la nueva versión sin copiar ni iniciar el JAR manualmente.

## 15. Limpiar las credenciales

IAM → usuario administrativo → **Security credentials** → **Access keys**.

1. Desactiva la access key usada por AWS CLI.
2. Elimínala si ya no será necesaria.
3. Conserva secretos y llaves privadas fuera del repositorio.

## Resultado

- Tabla `taskflow-eventos` activa.
- EC2 asociada al rol y tag requeridos.
- CodeBuild genera el JAR.
- CodeDeploy instala y valida la API.
- CodePipeline ejecuta el flujo con cada cambio publicado.
