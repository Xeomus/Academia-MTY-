# Despliegue de TaskFlow en AWS — Semana 5

Esta carpeta reúne dos guías prácticas para desplegar `taskflow-api` en AWS. El primer día configura la infraestructura y publica la API manualmente; el segundo incorpora una tabla de eventos en DynamoDB y automatiza la compilación y el despliegue.

## Contenido

| Día | Guía | Qué se realiza | Servicios principales |
| --- | --- | --- | --- |
| 1 | [EC2, S3 y RDS](./dia1.md) | Configurar la cuenta, ejecutar la API en EC2, guardar el JAR en S3 y conectar PostgreSQL en RDS. | IAM, Budgets, EC2, S3, RDS |
| 2 | [DynamoDB y despliegue automático](./dia2.md) | Crear y consultar una tabla de eventos, y desplegar la API desde GitHub mediante un pipeline. | IAM, EC2, DynamoDB, S3, CodeBuild, CodeDeploy, CodePipeline |

## Día 1 — Infraestructura y despliegue manual

La [guía del día 1](./dia1.md) comienza con un presupuesto y la protección de la cuenta mediante MFA e IAM. Después crea una instancia EC2 con Java 21, compila `taskflow-api` localmente y transfiere el JAR para iniciar la aplicación. También guarda el artefacto en un bucket S3 privado y conecta la API con una base PostgreSQL en RDS, cuyo acceso se limita al grupo de seguridad de EC2.

La guía incluye comandos, capturas y validaciones para comprobar el acceso por SSH, la integridad del JAR, Swagger y la conexión a la base de datos.

## Día 2 — DynamoDB y CI/CD

La [guía del día 2](./dia2.md) prepara un repositorio de GitHub y una instancia EC2 con permisos para el despliegue. Con AWS CLI crea la tabla `taskflow-eventos`, inserta registros y compara las operaciones `Query` y `Scan`.

Luego configura un bucket privado para artefactos, el agente de CodeDeploy y los archivos `buildspec.yml`, `appspec.yml`, `taskflow.service` y los scripts de despliegue. CodePipeline conecta el origen con CodeBuild y CodeDeploy; la verificación final consiste en publicar un cambio y comprobar la nueva versión en `/info`.

## Requisitos

- Cuenta de AWS con acceso a los servicios utilizados y una región asignada.
- Proyecto `taskflow-api` disponible localmente.
- Java 21, Maven, Git y SSH; AWS CLI para el día 2.
- Cuenta de GitHub para el flujo de despliegue del día 2.
- Un lugar seguro fuera del repositorio para llaves y credenciales.

Consulta los requisitos y valores concretos en cada guía antes de crear recursos. Los servicios de AWS pueden generar cargos; el día 1 explica cómo configurar un presupuesto y sus alertas.

## Estructura

```text
AWS/
├── README.md
├── dia1.md
├── dia2.md
└── img/
    ├── dia1/
    └── dia2/
```

Las imágenes de `img/` documentan los pasos de las dos guías. Los archivos de la aplicación y del pipeline mencionados en ellas se preparan en el proyecto `taskflow-api`; esta carpeta contiene la documentación y las capturas.
