# Automatización web con Selenium — Semana 5

La semana 5 reúne tres ejercicios de pruebas automatizadas con Selenium WebDriver, TestNG y Page Object Model. Esta guía se concentra en los archivos y métodos usados para localizar elementos, navegar, interactuar con las páginas y ejecutar las pruebas.

## Proyectos

| Día | Proyecto | Flujo automatizado | Guía |
| --- | --- | --- | --- |
| 1 | SauceDemo | Inicio de sesión, agregar un producto, abrir el carrito y eliminarlo. | [README](./QA%20Selenium/dia1/selenium/README.md) |
| 1 | OrangeHRM | Inicio de sesión, navegación al módulo PIM, búsqueda y selección de un resultado. | [README](./QA%20Selenium/dia1/selenium-Orange-HRM/README.md) |
| 2 | Santander | Recorrido por enlaces de Personas, Empresas, Pymes, Banca Privada y Acerca del Banco. | [README](./QA%20Selenium/dia2/selenium-santander/README.md) |

## Cómo se localizan e interactúan los elementos

Cada clase de `pages` guarda sus localizadores como constantes `By`. Los proyectos usan estrategias como `By.id`, `By.name`, `By.className` y `By.cssSelector` para identificar controles concretos sin colocar selectores dentro de las pruebas.

`base.BasePage` concentra las operaciones comunes:

- `find(By)` espera hasta obtener un elemento visible.
- `click(By)` localiza el elemento y hace clic.
- `write(By, String)` limpia un campo y escribe un valor.
- `text(By)` recupera el texto visible para validarlo.

El proyecto Santander amplía estas operaciones con selección de opciones, comprobación de elementos y manejo de elementos que cambiaron dentro del DOM.

## Cómo se navega

Los métodos públicos de las Page Objects representan acciones del usuario. Cuando una acción cambia de pantalla, el método puede devolver la Page Object de destino; por ejemplo, `LoginPage.login()` devuelve `HomePage` y `HomePage.goToCart()` devuelve `CartPage`. Esto permite encadenar un recorrido sin usar Selenium directamente desde la prueba.

Santander también controla las pestañas mediante los métodos de `base.BaseTest`: guarda las ventanas abiertas antes de hacer clic, detecta una ventana nueva, cambia hacia ella y vuelve a la página principal antes del siguiente recorrido.

## Cómo se automatizan las pruebas

`utils.DriverFactory` crea y configura `ChromeDriver`. Después, `base.BaseTest` usa `@BeforeMethod` para abrir el navegador y cargar la URL inicial. Las clases ubicadas en `src/test/java/tests` definen los escenarios con `@Test` y validan sus resultados mediante aserciones de TestNG.

Desde la carpeta de cualquiera de los proyectos:

```powershell
mvn clean test
```

Selenium Manager resuelve automáticamente el ChromeDriver compatible. Consulta el README de cada proyecto para conocer sus métodos y particularidades de ejecución.
