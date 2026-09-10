# Selenium Framework POM corregido

Proyecto de ejemplo con Java 21, Maven, Selenium WebDriver, TestNG y Page Object Model.

## Corrección principal

La prueba `LoginTest.java` y su clase base `BaseTest.java` están en `src/test/java`. Maven Surefire puede encontrarlas y ejecutar `@BeforeMethod`, que crea el navegador.

## Requisitos

- JDK 21 configurado en `JAVA_HOME` y en Eclipse.
- Maven 3.9 o superior.
- Google Chrome instalado.
- Acceso a Internet en la primera ejecución para que Selenium Manager resuelva ChromeDriver.

## Ejecutar en Eclipse

1. Importar con **File > Import > Existing Maven Projects**.
2. Seleccionar **Maven > Update Project**.
3. Abrir `src/test/java/tests/LoginTest.java`.
4. Ejecutar **Run As > TestNG Test**.

## Ejecutar con Maven

```bash
mvn clean test
```

El navegador abrirá SauceDemo, iniciará sesión, validará el título `Products` y se cerrará al finalizar.
