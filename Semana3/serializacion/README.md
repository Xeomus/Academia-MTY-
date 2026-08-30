# Serialización en Java

Este proyecto muestra de forma sencilla cómo guardar un objeto de Java en un archivo y cómo recuperarlo después.

## ¿Qué es la serialización?

La **serialización** convierte un objeto en una secuencia de datos que puede guardarse en un archivo o enviarse a otro sistema.

En este proyecto, un objeto `Player` se guarda en el archivo `player.ser`.

La **deserialización** realiza el proceso contrario: lee los datos del archivo y los convierte nuevamente en un objeto de Java.

El proceso puede resumirse así:

```text
Objeto Player -> serialización -> player.ser
player.ser -> deserialización -> Objeto Player
```

## ¿Cómo funciona el proyecto?

### Clase `Player`

La clase `Player` representa a un jugador y contiene los siguientes datos:

- Nombre
- Número
- Posición

La clase implementa la interfaz `Serializable`:

```java
public class Player implements Serializable
```

Esto permite que los objetos de esta clase puedan escribirse en un archivo mediante las herramientas de serialización de Java.

### Clase `Main`

El programa realiza los siguientes pasos:

1. Crea un objeto `Player` con los datos de Alejandro Zendejas.
2. Utiliza `ObjectOutputStream` para guardar el objeto en `player.ser`.
3. Utiliza `ObjectInputStream` para leer el archivo.
4. Convierte los datos recuperados nuevamente en un objeto `Player`.
5. Imprime el jugador recuperado en la consola.

Los bloques `try-with-resources` cierran automáticamente los archivos cuando termina cada operación.

## Estructura del proyecto

```text
serializacion/
|-- src/
|   |-- Main.java
|   `-- Player.java
|-- player.ser
`-- README.md
```

- `Main.java`: ejecuta la serialización y la deserialización.
- `Player.java`: define el objeto que se guarda.
- `player.ser`: archivo binario generado al serializar el jugador.

## Resultado esperado

La consola mostrará un resultado similar a este:

```text
Player serializated.
Player Saved:
Player {name = Alejandro Zendejas', number = 10, position = Extremo Derecho'}
```

Después de ejecutar el programa, `player.ser` contendrá el objeto guardado.

## Importante

`player.ser` es un archivo binario, por lo que no está diseñado para leerse o editarse como un archivo de texto. Para recuperar su contenido debe usarse `ObjectInputStream` y una clase compatible con la que se utilizó al guardarlo.
