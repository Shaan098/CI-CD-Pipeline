@echo off
REM DevOps Dashboard - Start Backend Server
echo ====================================
echo   Starting DevOps Backend Server
echo ====================================
echo.
echo Checking prerequisites...

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Maven is not installed or not in PATH
    echo Please install Apache Maven
    pause
    exit /b 1
)

echo [OK] Java and Maven are installed
echo.
echo Starting Spring Boot application...
echo Backend will be available at: http://localhost:8080
echo.
echo Press Ctrl+C to stop the server
echo.

mvn spring-boot:run

pause
