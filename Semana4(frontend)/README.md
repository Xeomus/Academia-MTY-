# Desarrollo frontend — Semana 4

Esta semana reúne ejercicios que avanzan desde los fundamentos de una interfaz web hasta una aplicación React conectada a una API con autenticación. El contenido está organizado por día y cada etapa incorpora nuevos conceptos sobre componentes, consumo de servicios, navegación y manejo de estado.

## Contenido

| Día | Proyecto | Descripción | Tecnologías principales |
| --- | --- | --- | --- |
| 1 | [Fundamentos web](./dia1/) | Páginas y componentes visuales creados sin framework, incluyendo tarjetas y consumo de APIs públicas. | HTML, CSS, JavaScript |
| 2 | [Rick and Morty API](./dia2/RickAndMortyAPI/) | Cuadrícula de personajes obtenidos desde la API de Rick and Morty. | React, TypeScript, Vite, Material UI |
| 3 | [React API REST](./dia3/ReactApiRest/) | Interfaz CRUD para consultar y modificar usuarios de JSONPlaceholder. | React, TypeScript, Fetch API, Material UI |
| 3 | [TaskFlow Draft](./dia3/taskFlowDraft/) | Primer ejercicio de conexión con el servicio de TaskFlow. | React, TypeScript, Fetch API |
| 4 | [JWT Auth Demo](./dia4/jwt-auth-demo/) | Cliente con inicio de sesión, sesión persistente, rutas protegidas y administración básica de proyectos. | React, TypeScript, Axios, JWT |
| 5 | [TaskFlow](./dia5/taskFlow/) | Aplicación final para administrar proyectos y sus tareas desde una interfaz protegida. | React, TypeScript, Material UI, React Router, Axios |

## Día 1 — Fundamentos web

El primer día contiene ejercicios de HTML, CSS y JavaScript ejecutados directamente en el navegador:

- La página principal presenta una tarjeta de perfil y manipulación básica del DOM.
- `cardsZapatos` practica la composición y el diseño de tarjetas de productos.
- `Cocktails` consulta una API pública de bebidas.
- `RickyMorty` consume la API de Rick and Morty con JavaScript.

Abre cualquiera de los archivos `index.html` en un navegador para revisar el ejercicio correspondiente. Para evitar restricciones del navegador al consumir APIs, también puedes servir la carpeta con una extensión como Live Server.

## Día 2 — React y consumo de APIs

[Rick and Morty API](./dia2/RickAndMortyAPI/) migra el consumo de datos a React. Utiliza `useEffect` y `useState` para solicitar personajes y Material UI para mostrar sus imágenes, estado, especie y género en tarjetas.

## Día 3 — Servicios REST y CRUD

[React API REST](./dia3/ReactApiRest/) separa las solicitudes HTTP en servicios y organiza la interfaz en páginas, componentes y hooks. Permite consultar, crear, editar y eliminar usuarios mediante JSONPlaceholder; al ser una API de demostración, las escrituras se simulan y no permanecen después de recargar.

[TaskFlow Draft](./dia3/taskFlowDraft/) es una primera integración con el endpoint de información de TaskFlow y muestra el manejo de estados de carga, respuesta y error.

## Día 4 — Autenticación con JWT

[JWT Auth Demo](./dia4/jwt-auth-demo/) incorpora inicio de sesión, contexto de autenticación, rutas protegidas y un cliente HTTP que adjunta el token a las solicitudes. También incluye pantallas iniciales para consultar y administrar proyectos y tareas.

Antes de ejecutarlo, copia `.env.example` como `.env` si necesitas cambiar la dirección del backend:

```powershell
Copy-Item .env.example .env
```

## Día 5 — TaskFlow

[TaskFlow](./dia5/taskFlow/) completa la interfaz para organizar proyectos y tareas. Incluye operaciones de creación, edición y eliminación, cambios de estado y prioridad, fechas límite, manejo de errores y navegación por proyecto.

La aplicación necesita una API compatible con TaskFlow. En desarrollo utiliza el proxy `/api`; para apuntar a otro servidor, copia `.env.example` como `.env` y modifica `VITE_API_URL`.

Consulta la [guía específica de TaskFlow](./dia5/taskFlow/README.md) para conocer sus características y configuración, o revisa su [documentación técnica](./dia5/taskFlow/docs.md) para ver la estructura interna y los contratos utilizados.

## Requisitos

- Node.js 20 o posterior.
- npm.
- Un navegador moderno.
- Acceso a Internet para instalar dependencias y consumir las APIs públicas.
- Una API compatible con TaskFlow para los ejercicios de los días 4 y 5.

## Ejecutar los proyectos React

Entra en la carpeta del proyecto que quieras iniciar. Por ejemplo:

```powershell
cd .\dia2\RickAndMortyAPI
npm install
npm run dev
```

Vite mostrará en la terminal la dirección local de la aplicación, normalmente `http://localhost:5173`.

Los mismos comandos se utilizan en los proyectos de los días 3, 4 y 5. Para comprobar un proyecto antes de entregar cambios, ejecuta:

```powershell
npm run lint
npm run build
```

## Estructura de la semana

```text
Semana4(frontend)/
├── dia1/
│   ├── cardsZapatos/
│   ├── Cocktails/
│   └── RickyMorty/
├── dia2/
│   └── RickAndMortyAPI/
├── dia3/
│   ├── ReactApiRest/
│   └── taskFlowDraft/
├── dia4/
│   └── jwt-auth-demo/
└── dia5/
    └── taskFlow/
```

## Conceptos principales

- Estructura semántica con HTML y estilos responsivos con CSS.
- Manipulación del DOM y solicitudes HTTP con JavaScript.
- Componentes, propiedades, estado y efectos en React.
- Tipado de datos y servicios con TypeScript.
- Estados de carga y error al consumir APIs REST.
- Navegación del lado del cliente con React Router.
- Autenticación mediante JWT y protección de rutas.
- Separación de páginas, componentes, hooks, contexto y servicios.
