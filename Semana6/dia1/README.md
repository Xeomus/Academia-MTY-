# Día 1 — GitHub Copilot CLI y TaskFlow

El primer día prepara un repositorio de TaskFlow para trabajar con GitHub Copilot CLI. La práctica se centra en explorar el código, controlar los permisos del agente y comprobar sus respuestas con comandos independientes.

## Requisitos

- Windows Terminal con PowerShell 7.
- Git, Java 21, Maven y Node.js 22 o posterior.
- Una cuenta de GitHub con acceso a GitHub Copilot.
- `academyMty` actualizado en `$HOME\academyMty`.

Sustituye `<tu-usuario>` por tu usuario de GitHub en minúsculas.

## Preparar el entorno

```powershell
$PSVersionTable.PSVersion.Major
git --version
java -version
mvn -version
node --version
npm --version
```

La primera línea debe devolver `7` y Maven debe utilizar Java 21. Instala la CLI e inicia sesión:

```powershell
npm install -g @github/copilot
copilot --version
copilot login
```

Si no se abre el navegador, utiliza `copilot login --device-code`.

## Crear el repositorio de trabajo

La copia se genera con `git archive` para incluir solamente archivos versionados:

```powershell
cd $HOME\academyMty
git pull
git archive --format=zip -o $HOME\taskflow-base.zip HEAD:taskflow-api
Expand-Archive $HOME\taskflow-base.zip -DestinationPath $HOME\taskflow-copilot-<tu-usuario>
Remove-Item $HOME\taskflow-base.zip
cd $HOME\taskflow-copilot-<tu-usuario>
```

Inicializa Git y evita versionar compilados, datos locales o secretos:

```powershell
'target/', 'data/', '.env', '*.pem', '*.ppk', '*.key', '.idea/', '*.iml', '.playwright-mcp/' | Set-Content .gitignore
git init -b main
git add -A
git ls-files | Select-String '(^|/)\.env$|\.(pem|ppk|key)$|\.idea/|\.iml$'
git commit -m "TaskFlow API, punto de partida"
```

La búsqueda no debe producir resultados. Crea en GitHub un repositorio público y vacío llamado `taskflow-copilot-<tu-usuario>` y conéctalo:

```powershell
git remote add origin https://github.com/<tu-usuario>/taskflow-copilot-<tu-usuario>.git
git push -u origin main
```

## Verificar el punto de partida

```powershell
mvn clean test
```

La suite debe terminar con `BUILD SUCCESS`. Este resultado servirá como referencia antes de permitir cambios del agente.

## Configurar Copilot

```powershell
[Environment]::SetEnvironmentVariable('COPILOT_MODEL','gpt-5-mini','User')
$env:COPILOT_MODEL
cd $HOME\taskflow-copilot-<tu-usuario>
copilot
```

Dentro de Copilot, `/model`, `/usage` y `/context` muestran la configuración de la sesión. Mantén otra pestaña de PowerShell para verificar lo que responda:

```powershell
Get-ChildItem src\main\java\com\taskflow -Directory | Select-Object -ExpandProperty Name
Get-ChildItem src -Recurse -Filter *.java | Select-String 'estaVencida'
Get-ChildItem src\main\java -Recurse -Filter '*Controller.java' | Select-String '@(Get|Post|Put|Patch|Delete)Mapping'
```

## Revisar permisos y cambios

Lee cada operación antes de aprobarla. Autoriza solo el comando o archivo necesario y rechaza borrados, commits o cambios que no pediste.

Dentro de Copilot, `/diff` muestra las modificaciones y `/rewind` permite volver a un punto anterior. Confirma siempre el resultado:

```powershell
git status --short
git diff
```

## Agregar instrucciones del repositorio

Ejecuta `/init` dentro de Copilot y después reemplaza el archivo generado por las instrucciones del curso:

```powershell
New-Item -ItemType Directory -Force .github | Out-Null
Copy-Item $HOME\academyMty\copilot\dia-1\copilot-instructions.md .github\copilot-instructions.md -Force
Select-String -Path .github\copilot-instructions.md -Pattern '^# Instrucciones de Copilot para TaskFlow API'
git add .github\copilot-instructions.md
git commit -m "Instrucciones de Copilot del curso"
git push
```

Abre una sesión nueva y ejecuta `/instructions`. La CLI debe mostrar `.github/copilot-instructions.md` como instrucción del repositorio.

## Documentar y verificar la arquitectura

Pide al agente crear únicamente `docs/ARQUITECTURA.md`. Comprueba sus referencias con el verificador:

```powershell
Test-Path docs\ARQUITECTURA.md
& $HOME\academyMty\copilot\dia-1\verificar-arquitectura.ps1
```

El objetivo es obtener `0 NO EXISTE`, pero esa salida solo confirma nombres y rutas. Revisa también el recorrido de creación de tareas:

```powershell
Select-String -Path src\main\java\com\taskflow\controller\TaskController.java -Pattern 'projectService.buscarPorId|taskService.crear'
Select-String -Path src\main\java\com\taskflow\service\TaskService.java -Pattern 'ProjectRepository'
Select-String -Path docs\ARQUITECTURA.md -Pattern 'buscarPorId'
```

El controlador debe comprobar el proyecto con `ProjectService.buscarPorId`; `TaskService` no debe depender de `ProjectRepository`.

## Comprobación final

```powershell
mvn test
git status --short
git push
```

La suite debe permanecer en verde y Git no debe mostrar archivos sensibles ni cambios inesperados.
