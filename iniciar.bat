@echo off
setlocal EnableExtensions EnableDelayedExpansion
cd /d "%~dp0"
title RegistroPlataforma - Backend

echo ============================================
echo  RegistroPlataforma - compilar y ejecutar
echo ============================================
echo.

REM ---------- 1. Java con el que se EJECUTA el servidor (el que ya tiene instalado) ----------
set "JAVA_RUN="
for /f "delims=" %%i in ('where java 2^>nul') do if not defined JAVA_RUN set "JAVA_RUN=%%i"
if not defined JAVA_RUN goto sinjava

REM ---------- 2. JDK con el que se COMPILA (un JDK 8 si existe, si no el javac que ya tenga) ----------
set "JDK_BUILD="
for /d %%d in ("C:\Program Files\Java\jdk1.8*") do if exist "%%~d\bin\javac.exe" set "JDK_BUILD=%%~d"
for /d %%d in ("C:\Program Files (x86)\Java\jdk1.8*") do if exist "%%~d\bin\javac.exe" set "JDK_BUILD=%%~d"
if defined JDK_BUILD goto hayjdk

set "JAVAC_P="
for /f "delims=" %%i in ('where javac 2^>nul') do if not defined JAVAC_P set "JAVAC_P=%%i"
if not defined JAVAC_P goto sinjdk
for %%i in ("!JAVAC_P!") do set "JDK_BUILD=%%~dpi.."

:hayjdk
REM Si el JDK encontrado es un JDK 8, tambien se usa su java para ejecutar
if exist "%JDK_BUILD%\bin\java.exe" (
  echo %JDK_BUILD% | findstr /I "jdk1.8" >nul
  if not errorlevel 1 set "JAVA_RUN=%JDK_BUILD%\bin\java.exe"
)

REM ---------- 3. Maven ----------
set "MVN="
for /f "delims=" %%i in ('where mvn.cmd 2^>nul') do if not defined MVN set "MVN=%%i"
if not defined MVN for /d %%d in ("C:\apache-maven-*") do if exist "%%~d\bin\mvn.cmd" set "MVN=%%~d\bin\mvn.cmd"
if not defined MVN for /d %%d in ("C:\apache-maven-*\apache-maven-*") do if exist "%%~d\bin\mvn.cmd" set "MVN=%%~d\bin\mvn.cmd"
if not defined MVN goto sinmaven

echo Compilador (JDK) : %JDK_BUILD%
echo Ejecucion (java) : %JAVA_RUN%
echo Maven            : %MVN%
echo.
echo Version de java que ejecutara el servidor:
"%JAVA_RUN%" -version
"%JAVA_RUN%" -version 2>&1 | findstr /C:"1.8." >nul
if errorlevel 1 echo AVISO: el java de ejecucion no parece ser Java 8. Revise la lista de arriba.
echo.

REM ---------- 4. Compilar (genera target\registroplataforma-backend-1.0.0.jar) ----------
echo Compilando el proyecto (la primera vez descarga dependencias, puede tardar varios minutos)...
set "JAVA_HOME=%JDK_BUILD%"
call "%MVN%" clean package -DskipTests
if errorlevel 1 goto fallo
if not exist "target\registroplataforma-backend-1.0.0.jar" goto fallo

REM ---------- 5. Ejecutar ----------
echo.
echo ============================================
echo  Servidor iniciando en http://localhost:8080
echo  Para detenerlo presione Ctrl+C
echo ============================================
"%JAVA_RUN%" -jar "target\registroplataforma-backend-1.0.0.jar"
goto fin

:sinjava
echo ERROR: no se encontro java en el PATH.
goto fin

:sinjdk
echo ERROR: no se encontro un compilador javac (JDK).
goto fin

:sinmaven
echo ERROR: no se encontro Maven. Extraiga el zip de Maven en C:\ (debe existir C:\apache-maven-3.9.16\bin\mvn.cmd)
goto fin

:fallo
echo.
echo ERROR: la compilacion fallo. Copie el texto desde la primera linea que diga ERROR.
goto fin

:fin
echo.
pause
