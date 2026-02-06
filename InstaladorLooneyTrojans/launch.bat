@echo off
echo Iniciando LooneyTrojans...

:: Establece la ruta correcta del JAR de SQLite y tu aplicación
set SQLITE_JAR=app\lib\sqlite-jdbc-3.49.1.0.jar
set APP_JAR=app\bin\LooneyTrojans.jar

:: Ejecuta la aplicación Java con los archivos JAR en el classpath
runtime\bin\java.exe -cp "%SQLITE_JAR%;%APP_JAR%" looneytrojans.LooneyTrojans

pause