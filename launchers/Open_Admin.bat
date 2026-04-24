@echo off
title Sistema Fichas - Admin
cd /d "%~dp0.."

set MAVEN_HOME=C:\Program Files\Apache NetBeans\java\maven
set JAVA_HOME=C:\Program Files\Apache NetBeans\jdk
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo Starting Admin module...
call "%MAVEN_HOME%\bin\mvn.cmd" javafx:run -Djavafx.args="admin" --no-transfer-progress

echo.
echo Application closed. Press any key to exit.
pause >nul
