# Semana 5 — AWS y QA con Selenium

Esta semana se trabaja en dos frentes: desplegar y automatizar la publicación de `taskflow-api` en AWS, y crear pruebas funcionales de sitios web con Selenium. Cada carpeta incluye sus propias guías y ejemplos.

## AWS

La práctica de [AWS](./AWS/README.md) se organiza en dos días:

| Día | Actividad | Guía |
| --- | --- | --- |
| 1 | Configurar un presupuesto y accesos IAM; desplegar `taskflow-api` con Java 21 en EC2; almacenar el JAR en un bucket S3 privado y conectar la API a PostgreSQL en RDS. | [EC2, S3 y RDS](./AWS/dia1.md) |
| 2 | Crear una tabla de eventos en DynamoDB y comparar `Query` con `Scan`; preparar un flujo de despliegue desde GitHub con CodeBuild, CodeDeploy y CodePipeline, y comprobar la versión publicada en `/info`. | [DynamoDB y CI/CD](./AWS/dia2.md) |

Las guías contienen comandos, capturas y validaciones para reproducir cada paso. Los recursos de AWS pueden generar cargos; el primer día incluye la configuración de un presupuesto y sus alertas.

## QA Selenium

La práctica de [QA Selenium](./QA%20Selenium/README.md) usa Java, Selenium WebDriver, TestNG y Page Object Model para separar los selectores y las acciones de las pruebas.

| Día | Proyecto | Qué se automatiza | Guía |
| --- | --- | --- | --- |
| 1 | SauceDemo | Inicio de sesión, agregar un producto al carrito y eliminarlo. | [SauceDemo](./QA%20Selenium/dia1/selenium/README.md) |
| 1 | OrangeHRM | Inicio de sesión, navegación al módulo PIM, búsqueda y selección de un resultado. | [OrangeHRM](./QA%20Selenium/dia1/selenium-Orange-HRM/README.md) |
| 2 | Santander | Navegación por los menús de Personas, Empresas, Pymes, Banca Privada y Acerca del Banco, con manejo de pestañas y capturas ante fallos. | [Santander](./QA%20Selenium/dia2/selenium-santander/README.md) |

En los tres proyectos, las Page Objects reúnen los localizadores y las interacciones; TestNG ejecuta los escenarios y sus aserciones. Cada proyecto tiene su propio `pom.xml` y README. Para ejecutar sus pruebas, entra en la carpeta correspondiente y usa `mvn clean test`. Se requiere Java, Maven y Chrome; Selenium Manager obtiene el controlador compatible.

## Estructura

```text
Semana5/
├── README.md
├── AWS/
│   ├── README.md
│   ├── dia1.md
│   └── dia2.md
└── QA Selenium/
    ├── README.md
    ├── dia1/
    │   ├── selenium/
    │   └── selenium-Orange-HRM/
    └── dia2/
        └── selenium-santander/
```
