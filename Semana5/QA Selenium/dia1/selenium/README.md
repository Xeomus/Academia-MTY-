# Pruebas de SauceDemo con Selenium

Este ejercicio automatiza únicamente los recorridos de inicio de sesión y carrito. La implementación separa los localizadores y las interacciones en Page Objects, mientras las pruebas describen el recorrido y sus validaciones.

## Archivos relevantes

| Archivo | Responsabilidad |
| --- | --- |
| `src/main/java/base/BasePage.java` | Esperar, localizar e interactuar con elementos. |
| `src/main/java/pages/LoginPage.java` | Capturar credenciales y enviar el formulario. |
| `src/main/java/pages/HomePage.java` | Validar la pantalla de productos, agregar el producto y abrir el carrito. |
| `src/main/java/pages/CartPage.java` | Eliminar el producto y comprobar si el carrito quedó vacío. |
| `src/main/java/utils/DriverFactory.java` | Crear y maximizar `ChromeDriver`. |
| `src/test/java/base/BaseTest.java` | Abrir SauceDemo antes de cada prueba. |
| `src/test/java/tests/LoginTest.java` | Automatizar y validar el inicio de sesión. |
| `src/test/java/tests/CartTest.java` | Automatizar el recorrido del carrito. |

## Cómo se atrapan los elementos

Las clases de `pages` declaran localizadores `By` privados y constantes. Se usa `By.id` para campos y botones con identificadores estables, y `By.className` para el título, el icono del carrito y sus elementos.

`BasePage.find(By locator)` aplica una espera explícita de hasta 10 segundos con `visibilityOfElementLocated`. Los demás métodos reutilizan esa búsqueda:

- `click(By)` espera el elemento y hace clic.
- `write(By, String)` limpia el campo y escribe el valor recibido.
- `text(By)` obtiene el texto visible.

`CartPage.isCartEmpty()` usa `driver.findElements(CART_ITEM).isEmpty()`: al buscar una colección puede comprobar la ausencia del producto sin provocar una excepción.

## Navegación e interacción

`LoginPage.login(username, password)` escribe en `USER_NAME` y `PASSWORD`, pulsa `LOGIN_BUTTON` y devuelve una instancia de `HomePage`.

En `HomePage`:

- `getProductsTitle()` obtiene el título que confirma el inicio de sesión.
- `addProduct()` hace clic en el botón de la mochila y devuelve la misma página.
- `goToCart()` pulsa el icono y devuelve `CartPage`.

En `CartPage`, `removeBackpack()` elimina el producto. Como `addProduct()` y `removeBackpack()` devuelven `this`, las acciones de una misma pantalla se pueden encadenar. Cuando cambia la pantalla, el método devuelve la Page Object correspondiente.

## Automatización de las pruebas

`BaseTest.setup()` se ejecuta antes de cada método gracias a `@BeforeMethod`: crea Chrome mediante `DriverFactory.createChromeDriver()` y abre `https://www.saucedemo.com/`.

`LoginTest.sholudLoginSuccessfully()` ejecuta `login()` y compara el título con `Products`. `CartTest.shouldAddAndRemoveProductFromCart()` inicia sesión, agrega la mochila, navega al carrito, la elimina y verifica que no queden elementos.

Desde esta carpeta:

```powershell
mvn clean test
```

Maven Surefire ejecuta las clases cuyo nombre termina en `Test.java`. Selenium Manager obtiene el ChromeDriver compatible. Actualmente `tearDown()` está comentado en `BaseTest`, así que Chrome no se cierra automáticamente al finalizar; para activar el cierre debe restaurarse el método con `@AfterMethod` y `driver.quit()`.
