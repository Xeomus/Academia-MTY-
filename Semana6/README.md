# Semana 6 — Desarrollo asistido con GitHub Copilot

Esta semana presenta un flujo de trabajo completo con agentes de inteligencia artificial aplicado a `taskflow-api`. Las actividades avanzan desde el uso básico de GitHub Copilot CLI hasta la creación de herramientas MCP, skills, agentes personalizados y un proyecto final desarrollado desde Visual Studio Code.

## Contenido

| Día | Tema | Descripción |
| --- | --- | --- |
| 1 | [Introducción a GitHub Copilot CLI](./dia1/) | Instalación de la CLI, exploración del repositorio, administración de permisos y verificación de las respuestas del agente. |
| 2 | [Especificación, implementación y revisión](./dia2/) | Desarrollo de endpoints a partir de especificaciones, revisión de cambios, planificación y trabajo mediante pull requests. |
| 3 | [Model Context Protocol](./dia3/) | Integración de servidores MCP para GitHub, AWS, Playwright y una API de TaskFlow. |
| 4 | [Skills y agentes personalizados](./dia4/) | Creación de instrucciones reutilizables, scripts de verificación y agentes con responsabilidades y permisos distintos. |
| 5 | [Visual Studio Code y proyecto final](./dia5/) | Uso de Copilot dentro de VS Code y aplicación del flujo completo en una funcionalidad final. |

## Organización de la semana

Cada carpeta corresponde a un día de trabajo y contiene una guía con los comandos necesarios, la explicación de cada etapa y las comprobaciones esperadas:

- [`dia1`](./dia1/): prepara el entorno y establece las prácticas para validar lo que afirma o modifica un agente.
- [`dia2`](./dia2/): aplica especificaciones y revisiones verificables al desarrollo de nuevas funcionalidades.
- [`dia3`](./dia3/): amplía las capacidades del agente mediante herramientas externas conectadas con MCP.
- [`dia4`](./dia4/): convierte instrucciones repetidas en skills y distribuye tareas entre agentes especializados.
- [`dia5`](./dia5/): traslada el flujo a VS Code y lo integra en el proyecto final de la academia.

## Conceptos principales

- GitHub Copilot CLI y GitHub Copilot en Visual Studio Code.
- Contexto, modelos, consumo y permisos de un agente.
- Especificaciones, planes, revisión de código y pull requests.
- Model Context Protocol (MCP).
- Automatización de navegador con Playwright.
- Skills y agentes personalizados.
- Verificación mediante comandos, pruebas y scripts reproducibles.
- Seguridad y revisión humana de acciones automatizadas.

## Requisitos generales

- PowerShell 7 y Windows Terminal.
- Git y una cuenta de GitHub con acceso a GitHub Copilot.
- Java 21 y Maven.
- Node.js 22 o posterior y npm.
- Visual Studio Code para las actividades del día 5.
- Google Chrome para las prácticas de automatización con Playwright.

## Estructura

```text
Semana6/
├── README.md
├── dia1/
│   └── README.md
├── dia2/
│   └── README.md
├── dia3/
│   └── README.md
├── dia4/
│   └── README.md
└── dia5/
    └── README.md
```
