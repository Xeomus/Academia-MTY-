# comparar.ps1

param(
[int]$N = 50
)

$BASE = "http://localhost:8074"

$FUENTE = Join-Path $PSScriptRoot "..\src\main\java\com\academymty\webflux\mono\repo\EmployeeRepository.java"

$Contenido = Get-Content $FUENTE -Raw

$Match = [regex]::Match($Contenido, 'ofMillis\([0-9]+\)')

if (-not $Match.Success) {
Write-Host "No pude leer LATENCIA de $FUENTE. Revisa que siga siendo Duration.ofMillis(N)." -ForegroundColor Red
exit 1
}

$LAT_MS = [regex]::Match($Match.Value, '[0-9]+').Value
$LAT = [math]::Round([double]$LAT_MS / 1000, 3)

$RespuestaHilo = Invoke-RestMethod "$BASE/api/hilo"
$NUCLEOS = $RespuestaHilo.hilosDisponibles

if (-not $NUCLEOS) {
Write-Host "No responde $BASE"
exit 1
}

function Medir {
param(
[string]$Ruta,
[string]$Nombre
)

$Ini = [datetime]::UtcNow
$Procesos = @()

for ($i = 1; $i -le $N; $i++) {
$Procesos += Start-Process -FilePath "curl.exe" -ArgumentList "-s", "-o", "NUL", "$BASE$Ruta" -PassThru -WindowStyle Hidden
}

$Procesos | Wait-Process

$Fin = [datetime]::UtcNow
$Tiempo = ($Fin - $Ini).TotalSeconds

Write-Host ("  {0,-12} {1,3} peticiones en {2,6:N2} s" -f $Nombre, $N, $Tiempo)
}

$esperado_bloq = [math]::Round(($N / [double]$NUCLEOS) * $LAT, 2)
$tandas = [math]::Round($N / [double]$NUCLEOS, 1)

Write-Host ""

Write-Host "  Tu maquina tiene $NUCLEOS nucleos, asi que el event loop de Netty tiene"
Write-Host "  ~$NUCLEOS hilos. Lanzamos $N peticiones CONCURRENTES a cada ruta."

Write-Host ""

Write-Host "  Prediccion antes de correrlo:"
Write-Host "    reactivo    -> ~${LAT}s   (ningun hilo espera: las $N se solapan)"
Write-Host "    bloqueante  -> ~${esperado_bloq}s   ($N peticiones / $NUCLEOS hilos = $tandas tandas de ${LAT}s)"

Write-Host ""

Medir "/api/employees/1" "reactivo"
Medir "/api/mvc/employees/1" "bloqueante"

Write-Host ""

Write-Host "  Cuadro la prediccion? Si tu maquina tiene mas nucleos, la diferencia es menor;"
Write-Host "  si tiene menos, es brutal. Prueba con: .\scripts\comparar.ps1 200"

Write-Host ""

Write-Host '  La leccion NO es "reactivo es rapido". Las dos rutas tardan 5 s en el dato.'
Write-Host "  La leccion es que el bloqueante DESPERDICIA los $NUCLEOS hilos que tiene, durmiendolos,"
Write-Host "  y por eso las peticiones hacen cola. El reactivo los suelta y no encola nada."

Write-Host ""

Write-Host "  Y ojo: el bloqueante no es un endpoint raro que inventamos. Es EXACTAMENTE el"
Write-Host "  codigo del proyecto 15 pegado dentro de una app WebFlux. Esa es la trampa del"
Write-Host "  proyecto: si tu repositorio bloquea (JPA/JDBC), WebFlux no te da nada."

Write-Host ""
