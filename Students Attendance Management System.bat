@echo off
cd /d "C:\Users\DELL\Desktop\PROJECT II"

echo Compiling Java files...
javac -cp "mysql-connector-j-9.2.0.jar;." AttendanceSystem.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed. Exiting...
    pause
    exit /b
)

echo Running Java program...
java -cp "mysql-connector-j-9.2.0.jar;." AttendanceSystem

pause
