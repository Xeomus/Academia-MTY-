$BASE = "http://localhost:8075"

function Titulo {
    param([string]$Texto)
    Write-Host "`n== $Texto ==" -ForegroundColor Cyan
}

Titulo "A. application/json -- vas a esperar 5 segundos EN SILENCIO"
Write-Host "   mira el reloj: no aparece nada hasta el final"
curl.exe -s "$BASE/api/lecturas"
Write-Host "`n"

Titulo "B. text/event-stream -- el MISMO Flux, una lectura por segundo"
Write-Host "   la bandera -N desactiva el buffer para ver el goteo"
curl.exe -N -s --max-time 8 "$BASE/api/lecturas/stream"
Write-Host "`n"

Titulo "C. filter sobre un flujo vivo: solo por encima de 30 C"
curl.exe -N -s --max-time 15 "$BASE/api/lecturas/alertas?umbral=30"
Write-Host "`n"

Titulo "D. takeUntil: se cierra SOLO cuando baja de 20 C"
Write-Host "   fijate en que el comando termina solo"
curl.exe -N -s --max-time 25 "$BASE/api/lecturas/hasta/20"
Write-Host "`n"

Titulo "E. collectList: el Flux se colapsa en UN solo valor"
curl.exe -s "$BASE/api/lecturas/resumen"
Write-Host "`n"

Write-Host "`nY ahora lo importante: abre http://localhost:8075 en el navegador" -ForegroundColor Yellow
Write-Host "y dale a los dos botones a la vez. Eso es lo que no se ve en una terminal.`n"