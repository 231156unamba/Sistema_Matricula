@echo off
setlocal enabledelayedexpansion
for /r %%i in (*.java) do (
    findstr /i ".entity." "%%i" >nul
    if !errorlevel! 1 (
        echo Reemplazando en: %%i
        powershell -Command "(Get-Content '%%i') -replace 'import com\.unamba\.matriculas\.entity', 'import com.unamba.matriculas.model' | Set-Content '%%i'"
    )
)
echo Proceso completado.
