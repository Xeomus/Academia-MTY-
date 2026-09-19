# Día 5 — Visual Studio Code y proyecto final

El último día utiliza en Visual Studio Code las instrucciones, skills, agentes y servidores MCP preparados durante la semana. Después se implementa una funcionalidad completa y se entrega mediante pull request.

## Preparar Visual Studio Code

```powershell
winget install --id Microsoft.VisualStudioCode -e
code --version
code --install-extension GitHub.copilot-chat
cd $HOME\taskflow-copilot-<tu-usuario>
code .
```

Autoriza el repositorio con **Trust Folder & Continue** e inicia sesión en GitHub. Copilot necesita un espacio de trabajo confiable para utilizar agentes, terminal y herramientas MCP.

## Probar autocompletado y chat

Abre `TaskService.java`, escribe el inicio de un método pequeño y observa la sugerencia de Copilot. `Tab` la acepta, `Esc` la descarta y `Alt+]` muestra otra alternativa.

Deshaz el ejercicio antes de continuar y confirma que no dejó cambios:

```powershell
git status --short
```

En modo **Ask**, consulta dónde se implementa `estaVencida` y verifica la respuesta:

```powershell
Get-ChildItem src -Recurse -Filter *.java | Select-String 'estaVencida'
```

En modo **Agent**, puedes solicitar la ejecución de las pruebas. Al terminar, comprueba que no haya editado archivos:

```powershell
mvn test
git status --short
```

## Comprobar instrucciones, skills y agentes

Desde el chat revisa que aparezcan las instrucciones del repositorio, las skills `crear-endpoint-taskflow` y `verificar-taskflow`, y los agentes `revisor` y `tester`.

Si una skill no aparece, comprueba su frontmatter:

```powershell
Get-Content .github\skills\crear-endpoint-taskflow\SKILL.md -TotalCount 8
Get-Content .github\skills\verificar-taskflow\SKILL.md -TotalCount 8
```

## Configurar MCP en VS Code

La CLI y VS Code utilizan archivos de configuración distintos:

```powershell
New-Item -ItemType Directory -Force .vscode | Out-Null
Copy-Item $HOME\academyMty\copilot\dia-5\.vscode\mcp.json .vscode\mcp.json
code .vscode\mcp.json
```

Si el servidor propio no encuentra su JAR, vuelve a compilarlo:

```powershell
cd taskflow-mcp
mvn package
cd ..
```

Inicia TaskFlow en la terminal integrada:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=h2"
```

Usa `listar_tareas_vencidas` desde el chat y compara su salida con la API:

```powershell
$token = (Invoke-RestMethod -Method Post http://localhost:8080/auth/login -ContentType 'application/json' -Body '{"username":"ana","password":"ana123"}').token
Invoke-RestMethod http://localhost:8080/tasks/overdue -Headers @{ Authorization = "Bearer $token" }
```

## Elegir el proyecto final

```powershell
code $HOME\academyMty\copilot\dia-5\proyecto-final\specs
```

| Opción | Endpoint | Resultado principal |
| --- | --- | --- |
| `search` | `GET /tasks/search?q=api` | Busca tareas por título. |
| `assignee` | `PATCH /tasks/{id}/assignee` | Cambia el responsable de una tarea. |
| `progress` | `GET /reports/progress` | Calcula el avance de cada proyecto. |

Sustituye `<feature>` por la opción elegida en los comandos siguientes.

## Crear la rama y versionar la especificación

```powershell
git switch main
git pull
git switch -c feature/<feature>
New-Item -ItemType Directory -Force specs, semana6 | Out-Null
Copy-Item $HOME\academyMty\copilot\dia-5\proyecto-final\specs\<feature>.md specs\
git add specs
git commit -m "spec: <feature> (proyecto final)"
```

## Implementar con la skill

```powershell
copilot -p "/crear-endpoint-taskflow Implementa specs/<feature>.md. Respeta nombres, reglas y tests; al terminar ejecuta mvn test." --allow-tool=write --allow-tool='shell(mvn:*)' --max-ai-credits 30 --share semana6\sesion-implementacion.md --disable-mcp-server taskflow --disable-mcp-server playwright --disable-mcp-server aws-knowledge
```

Verifica el trabajo de forma independiente:

```powershell
mvn test
git add src
git diff --cached --name-status main -- src/test
Select-String -Path semana6\sesion-implementacion.md -Pattern 'Skill "crear-endpoint-taskflow" loaded'
```

Los tests de la funcionalidad deben ser archivos nuevos. No aceptes modificaciones o eliminaciones de tests que ya existían en `main`.

## Revisar con el agente especializado

```powershell
git diff main --output=semana6\proyecto-final.diff -- src
copilot --agent revisor -p "Revisa semana6/proyecto-final.diff contra specs/<feature>.md." --max-ai-credits 30 --share semana6\revision.md
Select-String -Path semana6\revision.md -Pattern 'Veredicto:|Casos sin test'
git status --porcelain src
```

Comprueba cada hallazgo contra el código y la especificación. El revisor no debe modificar archivos.

## Ampliar la verificación REST

```powershell
Copy-Item $HOME\academyMty\copilot\dia-5\proyecto-final\verificar\casos-<feature>.ps1 .github\skills\verificar-taskflow\
code .github\skills\verificar-taskflow\verificar.ps1
```

Agrega al script junto a los demás casos:

```powershell
. (Join-Path $PSScriptRoot 'casos-<feature>.ps1')
```

Ejecuta la verificación completa:

```powershell
pwsh -NoProfile -File .github\skills\verificar-taskflow\verificar.ps1
```

Todos los casos de la funcionalidad elegida deben aparecer como `[OK]`.

## Seguridad, commit y pull request

```powershell
Select-String -Path semana6\*.md -Pattern 'security password','Bearer ey','ghp_','gho_','github_pat_','AKIA'
```

La búsqueda no debe mostrar secretos. Después publica la rama:

```powershell
git add src specs .github semana6
git commit -m "feat: <feature> (proyecto final)"
git push -u origin feature/<feature>
```

Crea el pull request, solicita Copilot Code Review y valida cada comentario antes de corregirlo. Tras el merge:

```powershell
git switch main
git pull
mvn test
pwsh -NoProfile -File .github\skills\verificar-taskflow\verificar.ps1
git log --oneline -1 --merges
```

El proyecto termina cuando la suite y la verificación REST pasan desde `main`, el pull request está integrado y no quedan secretos ni cambios pendientes.
