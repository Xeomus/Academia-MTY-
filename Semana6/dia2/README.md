# Día 2 — Especificar, implementar y revisar

El segundo día utiliza Copilot para implementar `GET /tasks/overdue` y `GET /tasks/unassigned`. Cada cambio parte de una especificación escrita y se revisa con pruebas, diferencias de Git y una ejecución real de la API.

## Preparar el repositorio

```powershell
cd $HOME\academyMty
git pull
Get-ChildItem copilot\dia-2
cd $HOME\taskflow-copilot-<tu-usuario>
git switch main
git pull
mvn test
```

Antes de continuar, la rama debe estar limpia y la suite debe terminar con `BUILD SUCCESS`.

## Implementar tareas vencidas

Versiona la especificación antes de generar código:

```powershell
git switch -c feature/overdue
New-Item -ItemType Directory specs -Force | Out-Null
Copy-Item $HOME\academyMty\copilot\dia-2\specs\overdue.md specs\
git add specs
git commit -m "spec: GET /tasks/overdue"
```

Abre Copilot desde la raíz y solicita la implementación exacta de `specs/overdue.md`. Después verifica el resultado:

```powershell
mvn test
git status --short
git diff --stat main
git diff --numstat main -- src/test
git diff main -- src/main | Select-String 'estaVencida|POR_FECHA'
git diff main -- src/test | Select-String '@Disabled'
```

El servicio debe filtrar con `Task::estaVencida`, ordenar con `TaskOrders.POR_FECHA` y agregar pruebas sin desactivar ni debilitar las existentes.

```powershell
git add src
git commit -m "feat: GET /tasks/overdue"
```

## Implementar tareas sin responsable

Parte de la rama anterior para conservar ambos endpoints:

```powershell
git switch -c feature/unassigned
Copy-Item $HOME\academyMty\copilot\dia-2\specs\unassigned.md specs\
git add specs
git commit -m "spec: GET /tasks/unassigned"
```

Dentro de Copilot utiliza `/plan` antes de implementar. El plan debe mencionar `SIN_ASIGNAR`, `POR_FECHA`, `sinResponsable`, el servicio, el controlador y sus pruebas.

Después de aprobar el plan, verifica la segunda funcionalidad respecto a la rama anterior:

```powershell
git diff --stat feature/overdue
git diff feature/overdue -- src/main | Select-String 'SIN_ASIGNAR|POR_FECHA'
git diff --numstat feature/overdue -- src/test
mvn test
```

El servicio debe devolver únicamente tareas sin responsable, ordenadas por fecha, dejando al final las que no tienen fecha límite.

```powershell
git add src
git commit -m "feat: GET /tasks/unassigned"
```

## Revisar el cambio

```powershell
git diff main -- src
git diff main -- src/test | Select-String '^-[^-]'
mvn test
```

Comprueba que solo hayan cambiado los archivos relacionados, que los tests validen el filtrado y el orden, y que no se hayan eliminado aserciones existentes.

Dentro de Copilot ejecuta una revisión sin permitir ediciones:

```text
/review Revisa solo lo que cambió en esta rama respecto a main. Busca tests modificados, reglas incumplidas y pruebas que no validen lo que indica su nombre. No edites archivos; lista cada hallazgo con archivo y línea.
```

Contrasta cada observación con el código. Aplica únicamente las correctas y vuelve a ejecutar `mvn test`.

## Publicar mediante pull request

```powershell
git push -u origin feature/unassigned
```

Crea un pull request con base `main`, revisa `Files changed` y solicita Copilot Code Review. Después del merge:

```powershell
git switch main
git pull
mvn test
git log --oneline -1
```

## Probar los endpoints

Inicia la aplicación con H2:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=h2"
```

Desde otra terminal, autentícate y consulta ambos endpoints:

```powershell
$login = Invoke-RestMethod -Method Post http://localhost:8080/auth/login -ContentType 'application/json' -Body '{"username":"ana","password":"ana123"}'
$headers = @{ Authorization = "Bearer $($login.token)" }
Invoke-RestMethod http://localhost:8080/tasks/overdue -Headers $headers
Invoke-RestMethod http://localhost:8080/tasks/unassigned -Headers $headers
```

Una petición sin token debe devolver `401`:

```powershell
try { Invoke-RestMethod http://localhost:8080/tasks/overdue } catch { $_.Exception.Response.StatusCode.value__ }
```

## Comprobación final

```powershell
mvn test
git status --short
```

La suite en verde es necesaria, pero el diff y la calidad de las pruebas son los que confirman que la implementación cumple la especificación.
