# Rick and Morty API — React

Ejercicio del día 2: consulta la API pública de Rick and Morty y presenta los personajes en tarjetas con React, TypeScript y Material UI.

## Qué muestra

Al abrir la página, `RickAndMortyService` solicita `https://rickandmortyapi.com/api/character`. Mientras llega la respuesta muestra un indicador de carga; después presenta la imagen, nombre, estado, especie, género e identificador de cada personaje recibido. La consulta requiere conexión a Internet.

## Ejecutar

Desde esta carpeta:

```powershell
npm install
npm run dev
```

Vite indicará la dirección local en la terminal. Para revisar el proyecto:

```powershell
npm run lint
npm run build
```

El punto de entrada es `src/App.tsx` y la consulta y visualización están en `src/RickAndMortyService.tsx`.
