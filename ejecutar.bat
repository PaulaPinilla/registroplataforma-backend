@echo off
setlocal EnableExtensions
cd /d "%~dp0"
title RegistroPlataforma - Backend

if not exist "target\registroplataforma-backend-1.0.0.jar" (
  echo Aun no esta compilado. Ejecute primero iniciar.bat
  pause
  exit /b 1
)
set "JAVA_RUN="
for /f "delims=" %%i in ('where java 2^>nul') do if not defined JAVA_RUN set "JAVA_RUN=%%i"
echo Servidor iniciando en http://localhost:8080  (Ctrl+C para detener)
"%JAVA_RUN%" -jar "target\registroplataforma-backend-1.0.0.jar"
pause
