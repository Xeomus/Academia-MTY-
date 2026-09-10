# Academia MTY — Xideral

Repositorio de ejercicios y proyectos desarrollados durante la academia. El contenido está organizado por semanas y avanza desde fundamentos de Java y desarrollo backend con Spring Boot hasta pruebas, programación reactiva y aplicaciones frontend con React.

## Contenido

| Semana | Tema o proyecto | Descripción | Tecnologías principales |
| --- | --- | --- | --- |
| 1 | [Pacman](./Semana1/Pacman/) | Juego de escritorio con distintas estrategias de movimiento para los fantasmas. | Java, Swing, Gradle |
| 2 | [Inyección de dependencias](./Semana2/inyeccionDependencias/src/) | Comparación entre dependencias creadas internamente y dependencias recibidas desde el exterior. | Java |
| 2 | [Liga MX API](./Semana2/ligamx/) | API REST para administrar equipos y jugadores con persistencia relacional. | Spring Boot, Spring Data JPA, MySQL |
| 2 | [Liga MX Mongo API](./Semana2/ligaMxMongo/) | Variante documental de la API de Liga MX. | Spring Boot, Spring Data MongoDB, MongoDB |
| 3 | [Threads](./Semana3/threads/) | Ejemplo de concurrencia mediante `Thread` y `Runnable`. | Java |
| 3 | [Serialización](./Semana3/serializacion/) | Escritura y recuperación de objetos Java en archivos binarios. | Java |
| 3 | [Spring Security](./Semana3/SpringSecurity/) | Ejemplos de autenticación con HTTP Basic, JWT y OAuth2 con Keycloak. | Spring Boot, Spring Security, MySQL, Keycloak |
| 3 | [Programación reactiva](<./Semana3/Programacion%20Reactiva/>) | Ejercicios con `Mono`, `Flux` y eventos enviados por el servidor. | Spring WebFlux, Reactor, Maven |
| 3 | [Pruebas unitarias](./Semana3/testing/) | Proyectos progresivos de pruebas con aserciones, parametrización y dobles de prueba. | Java, JUnit, Mockito, Maven |
| 4 | [Desarrollo frontend](<./Semana4(frontend)/>) | Ejercicios de HTML, CSS y JavaScript que evolucionan hacia aplicaciones React con APIs REST y autenticación. | React, TypeScript, Vite, Material UI |

## Proyectos por semana

### Semana 1 — Java y patrones de diseño

[Pacman](./Semana1/Pacman/) es una aplicación de escritorio construida con Java Swing. Separa el estado del juego, el tablero, las colisiones y las entidades, y aplica el patrón Strategy para implementar los distintos comportamientos de los fantasmas.

### Semana 2 — Persistencia y APIs REST

La segunda semana introduce la [inyección de dependencias](./Semana2/inyeccionDependencias/src/) y desarrolla dos versiones de una API de Liga MX:

- [Liga MX API](./Semana2/ligamx/), con persistencia relacional mediante JPA y MySQL.
- [Liga MX Mongo API](./Semana2/ligaMxMongo/), con documentos de MongoDB que incorporan la lista de jugadores.

Cada API incluye su propia guía y una colección de Postman para probar los endpoints.

### Semana 3 — Java avanzado y backend

La tercera semana reúne ejercicios sobre [hilos](./Semana3/threads/), [serialización](./Semana3/serializacion/), [seguridad](./Semana3/SpringSecurity/), [programación reactiva](<./Semana3/Programacion%20Reactiva/>) y [pruebas unitarias](./Semana3/testing/).

Los proyectos de Spring Security muestran tres estrategias de autenticación: HTTP Basic, tokens JWT emitidos por la API y OAuth2 con Keycloak. Los ejemplos de WebFlux comparan flujos reactivos y bloqueantes mediante `Mono`, `Flux` y Server-Sent Events. La sección de testing avanza desde los fundamentos de JUnit hasta el uso de Mockito.

### Semana 4 — Desarrollo frontend

La cuarta semana comienza con páginas estáticas en HTML, CSS y JavaScript, y continúa con aplicaciones creadas con React, TypeScript y Vite. Los ejercicios consumen APIs públicas, practican operaciones CRUD, implementan rutas protegidas con JWT y culminan en TaskFlow, una interfaz para administrar proyectos y tareas.

Consulta el [README de la semana 4](<./Semana4(frontend)/README.md>) para conocer el contenido de cada día y las instrucciones de ejecución.

## Requisitos

Los requisitos dependen del proyecto que se quiera ejecutar:

- Java 21 para los proyectos backend y los ejercicios actuales de Java.
- Node.js 20 o posterior y npm para los proyectos de React.
- MySQL para las APIs relacionales.
- MongoDB para `ligaMxMongo`.
- Docker para el ejemplo de OAuth2 con Keycloak.
- Postman o `curl`, opcionales, para probar las APIs.

Los proyectos de Java incluyen Gradle Wrapper o Maven Wrapper, por lo que no es necesario instalar estas herramientas globalmente.

## Ejecución rápida

### Proyectos Gradle

Desde la carpeta que contiene `gradlew.bat`:

```powershell
.\gradlew.bat test
.\gradlew.bat bootRun
```

Para el proyecto Pacman utiliza `./gradlew run` en Linux o macOS, o `gradlew.bat run` en Windows.

### Proyectos Maven

Desde la carpeta que contiene `mvnw.cmd`:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

### Proyectos React

Desde la carpeta del ejercicio:

```powershell
npm install
npm run dev
```

Los proyectos que consumen una API configurable incluyen un archivo `.env.example` con las variables necesarias.

## Estructura del repositorio

```text
Academia-MTY-Xideral/
├── Semana1/
│   └── Pacman/
├── Semana2/
│   ├── inyeccionDependencias/
│   ├── ligamx/
│   └── ligaMxMongo/
├── Semana3/
│   ├── threads/
│   ├── serializacion/
│   ├── SpringSecurity/
│   ├── Programacion Reactiva/
│   └── testing/
└── Semana4(frontend)/
    ├── dia1/
    ├── dia2/
    ├── dia3/
    ├── dia4/
    └── dia5/
```

## Tecnologías utilizadas

- Java 21, Swing, Gradle y Maven
- Spring Boot, Spring Web MVC y Spring WebFlux
- Spring Data JPA y Spring Data MongoDB
- Spring Security, JWT, OAuth2 y Keycloak
- MySQL y MongoDB
- JUnit, Mockito y Postman
- HTML, CSS y JavaScript
- React 19, TypeScript y Vite
- Material UI, React Router y Axios
