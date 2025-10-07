@echo off
echo SkyFiles Build Script
echo ====================

echo.
echo Checking for Java installation...

java -version >nul 2>&1
if %errorlevel% == 0 (
    echo Java found! Building SkyFiles...
    echo.
    
    echo Building the application...
    gradlew.bat build
    
    echo.
    echo Creating Windows executable...
    gradlew.bat packageReleaseExe
    
    echo.
    echo Build complete! Check the build/compose/binaries/main-release/exe/ folder for SkyFiles-Setup.exe
    echo.
    pause
) else (
    echo Java not found! Please install Java JDK 17 or higher.
    echo.
    echo You can download Java from:
    echo https://adoptium.net/temurin/releases/
    echo.
    echo After installing Java, run this script again.
    echo.
    pause
)

