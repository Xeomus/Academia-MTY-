# Inyección de Dependencias

Ejemplo sencillo de **inyección de dependencias en Java vanilla**.

## Objetivo

Mostrar la diferencia entre una clase que crea sus propias dependencias y otra que las recibe desde el exterior.

## Ejemplo

El programa simula un servicio que puede enviar mensajes utilizando diferentes medios:

* Correo electrónico
* SMS
* Paloma mensajera

Todos estos implementan la interfaz `MessageSender`.

De esta forma, son implementaciones intercambiables del mismo contrato.

Por otra parte, en la carpeta `src/before` se encuentra un ejemplo que **no utiliza inyección de dependencias**.

En este caso, `noInyectionDependency` crea directamente el objeto que necesita mediante:

```java
new EmailSenderNoDependency();
```

Esto hace que la clase quede directamente acoplada a `EmailSenderNoDependency`. Si se quisiera cambiar el método de envío, por ejemplo de correo electrónico a paloma mensajera,
sería necesario modificar la propia clase o crear una nueva.

En cambio, en la carpeta `src/after`, `MessageService` ya no crea el medio de envío. En su lugar, recibe un `MessageSender` mediante su constructor.

De esta forma, la dependencia se inyecta desde el exterior:

```java
MessageSender sender = new PigeonSender();

MessageService service = new MessageService(sender);
```

La última línea inyecta la dependencia mediante el constructor.

## Comportamiento

`MessageService` depende de la interfaz `MessageSender` y no de una implementación específica. Por eso podemos
cambiar el medio de envío:

```java
MessageSender sender = new SmsSender();
```
o:
```java
MessageSender sender = new EmailSender();
```
o:
```java
MessageSender sender = new PigeonSender();
```

La clase que use esta dependencia no necesita saber cómo funcionan internamente el correo, SMS o la paloma mensajera; solo
debe recibir un objeto que cumpla con el contrato de la interfaz `MessageSender`.

## Ventajas

Acoplamiento

Cada clase depende de una abstracción. La implementación concreta se decide fuera de ella, lo que permite
modificar el comportamiento sin cambiar `MessageService`.

Testeabilidad

Como `MessageService` no crea internamente un `EmailSender`, `SmsSender` o `PigeonSender`,
durante una prueba se le podría proporcionar una implementación falsa de `MessageSender`.

Por ejemplo, si se define una clase de prueba `FakeMessageSender`:

MessageSender fakeSender = new FakeMessageSender();

MessageService service = new MessageService(fakeSender);

Así podemos probar MessageService sin tener que enviar realmente un correo, SMS o paloma.

## Conclusión

En este proyecto, MessageService no decide si los mensajes se envían por correo, SMS o paloma mensajera. 
Esa decisión se realiza fuera de la clase y la implementación elegida se inyecta mediante el constructor.

Esto reduce el acoplamiento, permite intercambiar implementaciones y facilita las pruebas del código.

