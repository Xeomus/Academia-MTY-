# Threads en Java

Proyecto que muestra dos formas de crear y ejecutar tareas concurrentes en Java:

- Heredar de la clase `Thread`.
- Implementar la interfaz `Runnable`.

El programa simula una carrera entre Mario y Luigi. Cada corredor avanza en intervalos de 10 metros hasta completar 50 metros, mientras ambos se ejecutan de manera concurrente.

## Conceptos principales

### Heredar de `Thread`

`RunnerThread` extiende la clase `Thread`, por lo que el objeto representa tanto la tarea como el hilo que la ejecuta. Su método `run()` se inicia llamando directamente a `start()`.

```java
RunnerThread runner1 = new RunnerThread("Mario");
runner1.start();
```

Esta alternativa es sencilla, pero impide que la clase herede de otra clase debido a que Java solo permite herencia simple.

### Implementar `Runnable`

`RunnerRunnable` implementa `Runnable` y representa únicamente la tarea. Para ejecutarla concurrentemente, debe entregarse a un objeto `Thread`.

```java
RunnerRunnable runner2 = new RunnerRunnable("Luigi");
Thread runner2Thread = new Thread(runner2);
runner2Thread.start();
```

Este enfoque permite que `RunnerRunnable` herede de `Person` y, al mismo tiempo, pueda ser ejecutada por un hilo. También separa la tarea del mecanismo que la ejecuta.

## Estructura del proyecto

```text
threads/
├── src/
│   ├── Main.java
│   ├── Person.java
│   ├── RunnerRunnable.java
│   └── RunnerThread.java
└── README.md
```

- `Main.java`: crea y pone en marcha los dos hilos.
- `Person.java`: clase base que almacena el nombre de una persona.
- `RunnerThread.java`: corredor implementado mediante herencia de `Thread`.
- `RunnerRunnable.java`: corredor que hereda de `Person` e implementa `Runnable`.

## Requisitos

- Java Development Kit (JDK) 8 o una versión posterior.

## `start()` frente a `run()`

Para iniciar un nuevo hilo se debe utilizar `start()`. Este método crea una nueva ruta de ejecución y hace que Java invoque `run()` automáticamente. Si se llama a `run()` directamente, el código se ejecuta en el hilo actual y no existe concurrencia.

## Interrupciones

Cada corredor utiliza `Thread.sleep(1000)` para esperar un segundo entre avances. Si el hilo es interrumpido durante la espera, se captura `InterruptedException`, se muestra un mensaje y finaliza la ejecución de ese corredor.
