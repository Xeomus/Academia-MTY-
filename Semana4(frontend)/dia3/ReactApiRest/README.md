# React API REST — usuarios

Ejercicio del día 3: interfaz CRUD de usuarios construida con React, TypeScript y Material UI. Usa la API de demostración JSONPlaceholder.

## Funcionalidad

La pantalla principal lista usuarios y permite crear, editar y eliminar registros. `src/services/api.ts` centraliza las solicitudes HTTP; `src/services/userService.ts` define las operaciones y `src/hooks/useUsers.ts` administra los estados de carga, error y guardado.

JSONPlaceholder simula las solicitudes de escritura: los cambios se reflejan en la sesión de la interfaz, pero no persisten en el servidor al recargar la página. Se necesita conexión a Internet.

## Ejecutar

Desde esta carpeta:

```powershell
npm install
npm run dev
```

Para comprobar el proyecto:

```powershell
npm run lint
npm run build
```

La vista que se monta actualmente desde `src/App.tsx` es `src/pages/UsersPage.tsx`. También hay componentes y servicios de publicaciones en el código, pero no forman parte de la pantalla principal.
