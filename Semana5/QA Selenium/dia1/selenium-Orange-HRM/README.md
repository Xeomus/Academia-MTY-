# Pruebas de OrangeHRM con Selenium

Este ejercicio automatiza el inicio de sesión y un recorrido por el módulo PIM. Los selectores y acciones viven en Page Objects; las clases de prueba coordinan el flujo y realizan las validaciones.

## Archivos relevantes

| Archivo | Responsabilidad |
| --- | --- |
| `src/main/java/base/BasePage.java` | Esperar, localizar e interactuar con elementos. |
| `src/main/java/pages/LoginPage.java` | Completar y enviar el inicio de sesión. |
| `src/main/java/pages/HomePage.java` | Leer el título del Dashboard y abrir PIM. |
| `src/main/java/pages/PimPage.java` | Buscar un empleado y seleccionar el primer resultado. |
| `src/main/java/utils/DriverFactory.java` | Crear y maximizar `ChromeDriver`. |
| `src/test/java/base/BaseTest.java` | Abrir OrangeHRM antes de cada prueba. |
| `src/test/java/tests/LoginTest.java` | Validar el inicio de sesión. |
| `src/test/java/tests/PimTest.java` | Automatizar el recorrido por PIM. |

## Cómo se atrapan los elementos

Los localizadores se declaran como constantes `By` dentro de cada Page Object:

- `LoginPage` usa `By.name` para usuario y contraseña, y `By.className` para el botón.
- `HomePage` usa `By.className` para el título y `By.cssSelector` para el enlace cuyo `href` contiene `viewPimModule`.
- `PimPage` usa selectores CSS basados en atributos, clases y posición para los campos, el botón de búsqueda y la primera casilla de resultados.

`BasePage.find(By locator)` espera hasta 10 segundos a que el elemento sea visible. `click()`, `write()` y `text()` pasan siempre por `find()`, por lo que las interacciones no dependen de pausas fijas.

## Navegación e interacción

`LoginPage.login(username, password)` escribe las credenciales, hace clic en el botón y devuelve `HomePage`. `HomePage.goToPim()` pulsa el enlace del módulo y devuelve `PimPage`.

Los métodos que permanecen en PIM devuelven la misma instancia para poder encadenarse:

- `search(name, id)` llena los dos filtros y pulsa buscar.
- `checkResult()` selecciona la casilla del primer resultado.

`getDashBoardTitle()` y `getPimTitle()` recuperan los encabezados que las pruebas usan para confirmar que la navegación llegó a la pantalla esperada.

## Automatización de las pruebas

`BaseTest.setup()`, anotado con `@BeforeMethod`, crea Chrome y abre la pantalla de acceso de OrangeHRM antes de cada prueba.

`LoginTest.sholudLoginSuccessfully()` valida que el encabezado sea `Dashboard`. `PimTest.Task1()` inicia sesión, confirma el Dashboard, abre PIM, valida el título, ejecuta la búsqueda y marca el primer resultado.

Desde esta carpeta:

```powershell
mvn clean test
```

Maven Surefire descubre las clases `*Test.java` y TestNG ejecuta sus métodos `@Test`. Selenium Manager resuelve ChromeDriver. Actualmente el `tearDown()` de `BaseTest` está comentado, por lo que el navegador no se cierra automáticamente al terminar.
