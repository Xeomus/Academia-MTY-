# TaskFlow Draft

Primer ejercicio de conexión con TaskFlow del día 3. Esta aplicación React y TypeScript consulta el endpoint `/info` de un despliegue de TaskFlow y muestra los campos `version` y `app`.

## Funcionamiento

`src/taskFlowService.tsx` realiza la solicitud al cargar la página. Muestra un mensaje de carga mientras espera la respuesta y un mensaje de error si la solicitud falla. La URL del servicio está escrita en ese archivo; para usar otro despliegue hay que cambiarla allí. El ejercicio requiere conexión a Internet y acceso al servicio indicado.

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

Esta es una práctica inicial; la aplicación de administración de proyectos y tareas se encuentra en [TaskFlow](../../dia5/taskFlow/README.md).
