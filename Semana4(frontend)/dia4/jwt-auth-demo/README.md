# JWT Auth Demo

Cliente React y TypeScript del día 4 para practicar inicio de sesión con JWT, rutas protegidas y consultas a una API de TaskFlow. Incluye vistas de proyectos y tareas.

## Requisitos y configuración

Necesitas Node.js 20 o posterior, npm y una API compatible que exponga `/auth/login` y los recursos de proyectos y tareas. La dirección base se lee de `VITE_API_URL`. Copia el ejemplo si necesitas configurarla:

```powershell
Copy-Item .env.example .env
```

Edita `.env` para apuntar a tu API. El archivo se mantiene fuera de Git. La sesión guarda el token en `localStorage`; `src/services/authService.ts` gestiona el inicio de sesión y `src/ProtectedRoute.tsx` restringe las páginas internas.

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

Las rutas principales son `/login`, `/dashboard` y `/projects/:projectId/tasks`. La aplicación completa de la siguiente jornada está en [TaskFlow](../../dia5/taskFlow/README.md).
