# probar.ps1
# Recorrido por todos los endpoints del proyecto 01.
# Asegurate de arrancar la app primero en otra consola.
$BASE = "http://localhost:8074"

function Titulo {
    param([string]$Texto)
    Write-Host "`n== $Texto ==" -ForegroundColor Cyan
}

Titulo "1. Un empleado que SI existe  (200)"
curl.exe -s -w "`n   -> HTTP %{http_code} en %{time_total}s`n" "$BASE/api/employees/1"

Titulo "2. Un empleado que NO existe  (404 con mensaje)"
curl.exe -s -w "`n   -> HTTP %{http_code}`n" "$BASE/api/employees/999"

Titulo "3. TRAMPA: un Mono vacio NO da 404, da 200 vacio"
curl.exe -s -w "   -> HTTP %{http_code} <- 200, no 404. El vacio no es un error.`n" "$BASE/api/employees-suave/999"
Write-Host "      Si quieres 404, tienes que pedirlo con switchIfEmpty() (endpoint 2)."

Titulo "4. El canal de error: truena, pero onErrorResume lo rescata"
curl.exe -s -w "`n   -> HTTP %{http_code} (fijate en el plan B)`n" "$BASE/api/employees/1/boom"

Titulo "5. Mono<Void>: no devuelve nada, solo avisa que termino"
curl.exe -s -w "   -> HTTP %{http_code} sin cuerpo`n" -X DELETE "$BASE/api/employees/1"

Titulo "6. Quien te atiende (llamalo varias veces)"
for ($i = 1; $i -le 5; $i++) {
    # Aqui usamos la magia de PowerShell para leer el JSON directamente
    $respuesta = curl.exe -s "$BASE/api/hilo" | ConvertFrom-Json
    Write-Host "   -> $($respuesta.hilo)"
}
Write-Host "   -> Se repiten los mismos nombres: eso es el event loop."