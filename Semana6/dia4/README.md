# Día 4 — Skills y agentes personalizados

El cuarto día convierte instrucciones recurrentes en skills y distribuye la implementación, las pruebas y la revisión entre agentes con permisos diferentes.

## Crear la rama y copiar la especificación

```powershell
cd $HOME\taskflow-copilot-<tu-usuario>
git switch main
git pull
git switch -c dia4-equipo
New-Item -ItemType Directory -Force specs, evidencia\dia4 | Out-Null
Copy-Item $HOME\academyMty\copilot\dia-3\issues\summary.md specs\summary.md
git add specs
git commit -m "spec: GET /projects/{id}/summary"
```

Versionar primero la especificación permite revisarla por separado de la implementación.

## Instalar las skills

```powershell
New-Item -ItemType Directory -Force .github\skills | Out-Null
Copy-Item -Recurse -Force $HOME\academyMty\copilot\dia-4\.github\skills\crear-endpoint-taskflow, $HOME\academyMty\copilot\dia-4\.github\skills\verificar-taskflow .github\skills\
copilot skill list
```

La lista debe mostrar `crear-endpoint-taskflow` y `verificar-taskflow`. Cada skill vive en `.github/skills/<nombre>/SKILL.md`; su frontmatter define cuándo se carga y el resto del archivo describe el procedimiento.

## Implementar el resumen del proyecto

Ejecuta la skill de forma explícita y limita las herramientas disponibles:

```powershell
copilot -p "/crear-endpoint-taskflow Implementa la especificación de specs/summary.md." --allow-tool=write --allow-tool='shell(mvn:*)' --max-ai-credits 30 --share evidencia\dia4\summary-sesion.md
```

Valida la implementación sin confiar únicamente en el mensaje final:

```powershell
mvn test
git status --porcelain src
Select-String -Path evidencia\dia4\summary-sesion.md -Pattern 'Skill "crear-endpoint-taskflow" loaded'
```

Los tests nuevos deberían aparecer como archivos agregados; los existentes no deben modificarse para hacer pasar el cambio.

```powershell
git add .github src specs
git commit -m "feat: GET /projects/{id}/summary"
```

## Ejecutar la verificación de TaskFlow

La skill `verificar-taskflow` incluye un script que compila la aplicación, la inicia con H2, prueba los endpoints y la detiene:

```powershell
pwsh -NoProfile -File .github\skills\verificar-taskflow\verificar.ps1
```

La salida debe terminar en `RESULTADO: 8/8 OK`. Si la máquina necesita más tiempo:

```powershell
pwsh -NoProfile -File .github\skills\verificar-taskflow\verificar.ps1 -EsperaMaxSeg 300
```

## Agregar agentes especializados

```powershell
New-Item -ItemType Directory -Force .github\agents | Out-Null
Copy-Item -Force $HOME\academyMty\copilot\dia-4\.github\agents\revisor.agent.md, $HOME\academyMty\copilot\dia-4\.github\agents\tester.agent.md .github\agents\
git add .github
git commit -m "chore: agentes revisor y tester"
```

Dentro de Copilot, `/agent` debe mostrar ambos agentes. El revisor solo dispone de lectura y búsqueda; el tester puede editar pruebas y ejecutar Maven.

## Revisar el cambio

Genera un diff limitado al código y entrégalo al revisor:

```powershell
git diff main --output=evidencia\dia4\summary.diff -- src
copilot --agent revisor -p "Revisa evidencia/dia4/summary.diff contra la especificación specs/summary.md." --max-ai-credits 30 --share evidencia\dia4\revision.md
Select-String -Path evidencia\dia4\revision.md -Pattern 'Veredicto:|Casos sin test'
git status --porcelain src
```

El último comando no debe mostrar cambios: el revisor no tiene permiso para editar. Comprueba cada hallazgo contra el código antes de aceptarlo.

## Completar las pruebas

```powershell
copilot --agent tester -p "Lee specs/summary.md, la revisión y los tests del endpoint. Agrega únicamente los casos que falten y ejecuta mvn test." --allow-tool=write --allow-tool='shell(mvn:*)' --max-ai-credits 30 --share evidencia\dia4\tester-sesion.md
git diff --numstat -- src/test
git status --porcelain src/main
mvn test
```

`src/main` debe permanecer sin cambios durante esta etapa. Revisa cada prueba nueva para confirmar que sus aserciones validan el comportamiento descrito.

## Escanear secretos y publicar

```powershell
Select-String -Path evidencia\dia4\* -CaseSensitive -Pattern 'AKIA[0-9A-Z]{16}|aws_secret_access_key|SecretAccessKey|generated security password|Bearer ey'
```

La búsqueda no debe devolver resultados. Después ejecuta nuevamente el verificador y publica la rama:

```powershell
pwsh -NoProfile -File .github\skills\verificar-taskflow\verificar.ps1
git add -A
git commit -m "test: revisión y casos de summary"
git push -u origin dia4-equipo
```

Crea un pull request y revisa `Files changed`. Tras el merge:

```powershell
git switch main
git pull
copilot skill list
mvn test
```

Las skills y los agentes deben quedar disponibles desde `main`.
