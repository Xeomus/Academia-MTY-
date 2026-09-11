# Pruebas de navegación de Santander con Selenium

Este ejercicio automatiza los enlaces de los menús Personas, Empresas, Pymes, Banca Privada y Acerca del Banco. La documentación se limita a los localizadores, los métodos de navegación, el control de ventanas y la ejecución de las pruebas.

## Archivos relevantes

| Archivo | Responsabilidad |
| --- | --- |
| `src/main/java/base/BasePage.java` | Buscar elementos visibles y ejecutar interacciones resistentes a cambios del DOM. |
| `src/main/java/pages/PersonasPage.java` | Abrir el menú Personas y navegar por sus enlaces. |
| `src/main/java/pages/EmpresasPage.java` | Navegar por Empresas y Gobierno y Multinacionales. |
| `src/main/java/pages/PymesPage.java` | Navegar por las opciones del menú Pymes. |
| `src/main/java/pages/BancaPrivadaPage.java` | Abrir el enlace de Banca Privada. |
| `src/main/java/pages/AcercaDelBancoPage.java` | Navegar por las opciones institucionales. |
| `src/test/java/base/BaseTest.java` | Administrar el navegador, la página inicial y las ventanas. |
| `src/test/java/tests/SantanderNavigationTest.java` | Ejecutar y agrupar todos los recorridos. |
| `src/test/java/listeners/ScreenshotListener.java` | Guardar una captura cuando falla una prueba. |
| `testng.xml` | Registrar el listener y seleccionar la clase de prueba. |

## Cómo se atrapan los elementos

Las Page Objects conservan cada selector en una constante `By`. Los botones principales se localizan por `id`; los enlaces se buscan principalmente con selectores CSS que comparan fragmentos de `href`, por ejemplo `a[href*='tarjetas-de-credito']`.

`BasePage.find(By locator)` consulta `findElements()` durante un máximo de 12 segundos, recorre las coincidencias y devuelve la primera que esté visible. Si una referencia quedó obsoleta por un cambio del DOM, ignora ese elemento y vuelve a consultar.

Los métodos comunes son:

- `click(By)`: espera un elemento visible y habilitado, lo centra con JavaScript y hace clic. Si no es interactuable, usa un clic JavaScript; si la referencia caduca, vuelve a localizarlo.
- `write(By, String)`: limpia y escribe en un campo.
- `text(By)`: recupera el texto visible.
- `selectByText(By, String)`: elige una opción de un `<select>` por su texto.
- `isSelected(By)`: consulta el estado de selección.
- `exists(By)`: comprueba si hay coincidencias sin exigir visibilidad.

## Cómo se navega

Cada clase de página expone métodos `goTo...()`. En los menús desplegables, un método privado como `openPeopleMenu()`, `openCompaniesMenu()`, `openSmesMenu()` u `openAboutMenu()` abre primero el menú; después el método público pulsa el enlace solicitado.

`SantanderNavigationTest.verifyNavigation()` recibe la navegación como `Runnable`, normalmente con una referencia como `page::goToCreditCards`. Para cada enlace:

1. `returnHome()` cierra ventanas secundarias y vuelve a la URL principal.
2. `getOpenWindows()` guarda los identificadores existentes.
3. `navegacion.run()` ejecuta el método de la Page Object.
4. `adjustWindowAfterNavigation()` detecta una pestaña nueva y cambia el WebDriver hacia ella.

Si el sitio cerró las ventanas o se perdió la sesión de Chrome, `restartDriver()` crea un navegador nuevo. Al terminar cada prueba, `tearDown()` ejecuta `driver.quit()`.

## Automatización y resultados

`SantanderNavigationTest` agrupa los recorridos en nueve métodos `@Test` ordenados por prioridad. Cada grupo utiliza `SoftAssert`: registra los enlaces que fallan y continúa con el resto antes de ejecutar `assertAll()`.

`testng.xml` registra `ScreenshotListener` y ejecuta `SantanderNavigationTest`. Cuando una prueba falla, `onTestFailure()` obtiene el WebDriver desde `BaseTest`, toma la captura y la guarda en `test-output/screenshots/` con el nombre del método y una marca de tiempo.

Ejecución normal:

```powershell
mvn clean test
```

Ejecución sin mostrar Chrome:

```powershell
mvn clean test -Dheadless=true
```

Los reportes de TestNG quedan en `test-output/index.html` y `test-output/emailable-report.html`. Selenium Manager obtiene automáticamente el ChromeDriver compatible.
