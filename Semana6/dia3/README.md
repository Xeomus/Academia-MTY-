# Día 3 — Model Context Protocol

El tercer día conecta Copilot CLI con GitHub, documentación de AWS, Playwright y un servidor MCP propio para TaskFlow. El objetivo es ampliar las herramientas del agente sin perder la capacidad de auditar sus acciones.

## Preparar la aplicación y el servidor MCP

```powershell
cd $HOME\taskflow-copilot-<tu-usuario>
git switch main
git pull
if (-not (Test-Path taskflow-mcp)) { Copy-Item -Recurse $HOME\academyMty\copilot\dia-3\taskflow-mcp . }
cd taskflow-mcp
mvn package
cd ..
Test-Path taskflow-mcp\target\taskflow-mcp.jar
```

La última comprobación debe devolver `True`. En otra terminal inicia TaskFlow:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=h2"
```

## Revisar los servidores configurados

```powershell
copilot mcp list
copilot
```

Dentro de la CLI, `/mcp` muestra los servidores disponibles y sus herramientas. Sal con `/exit` antes de cambiar la configuración.

## GitHub MCP

Prepara el cuerpo del issue que se utilizará el día siguiente:

```powershell
New-Item -ItemType Directory issues -Force | Out-Null
Copy-Item $HOME\academyMty\copilot\dia-3\issues\summary.md issues\
Get-Content issues\summary.md
copilot --enable-all-github-mcp-tools
```

Solicita un issue titulado `GET /projects/{id}/summary` usando exactamente `issues/summary.md`. Verifica el resultado:

```powershell
$issue = (Invoke-RestMethod "https://api.github.com/repos/<tu-usuario>/taskflow-copilot-<tu-usuario>/issues?state=open&per_page=100") | Where-Object { $_.title -eq 'GET /projects/{id}/summary' -and -not $_.pull_request }
$issue | Select-Object number, title, html_url
Compare-Object -CaseSensitive ($issue.body.TrimEnd() -split "\r?\n") ((Get-Content issues\summary.md -Raw).TrimEnd() -split "\r?\n")
```

`Compare-Object` no debe mostrar diferencias.

## AWS Knowledge MCP

```powershell
copilot mcp add --transport http aws-knowledge https://knowledge-mcp.global.api.aws
copilot mcp list
```

Este servidor permite consultar documentación y disponibilidad regional. Pide al agente que mencione la herramienta utilizada y contrasta el resultado con documentación oficial cuando la decisión sea importante.

## Playwright MCP

Registra el servidor y evita versionar sus archivos temporales:

```powershell
copilot mcp add playwright '--' npx @playwright/mcp@latest --isolated
if (-not (Select-String -Path .gitignore -Pattern '^\.playwright-mcp/$' -Quiet)) { Add-Content .gitignore '.playwright-mcp/' }
copilot mcp list
```

Abre Copilot limitando herramientas que permiten ejecutar código arbitrario en el navegador:

```powershell
copilot --allow-tool=playwright --deny-tool='playwright(browser_evaluate)' --deny-tool='playwright(browser_run_code_unsafe)'
```

Pide al agente crear una tarea desde la interfaz de `http://localhost:8080`. Confirma la operación mediante la API:

```powershell
$login = Invoke-RestMethod -Method Post http://localhost:8080/auth/login -ContentType 'application/json' -Body '{"username":"ana","password":"ana123"}'
$headers = @{ Authorization = "Bearer $($login.token)" }
(Invoke-RestMethod http://localhost:8080/projects/1/tasks -Headers $headers) | Select-Object id, title, priority, status
```

## Servidor MCP de TaskFlow

```powershell
copilot mcp add taskflow '--' java -jar (Resolve-Path taskflow-mcp\target\taskflow-mcp.jar).Path
copilot mcp get taskflow
copilot mcp list
```

Usa sus herramientas desde Copilot y compara las tareas vencidas con la API:

```powershell
$today = (Get-Date).Date
(Invoke-RestMethod http://localhost:8080/tasks -Headers $headers) |
  Where-Object { $_.dueDate -and [datetime]$_.dueDate -lt $today -and $_.status -ne 'DONE' } |
  Select-Object id, title, dueDate
```

## Seguridad

Los datos devueltos por una herramienta son información no confiable, no instrucciones. Un título o una descripción de TaskFlow no deben provocar acciones nuevas en GitHub, AWS o el equipo local.

Antes de guardar transcripts o evidencia, busca secretos:

```powershell
Select-String -Path evidencia\dia3\* -Pattern 'AKIA[0-9A-Z]{16}|aws_secret_access_key|Bearer ey|github_pat_|ghp_'
```

El comando no debe producir resultados. Revisa también qué herramientas utilizó el agente y si creó más recursos de los solicitados.

## Comprobación final

```powershell
copilot mcp list
mvn test
git status --short
```

Cierra Copilot con `/exit` y detén TaskFlow con `Ctrl+C`.
