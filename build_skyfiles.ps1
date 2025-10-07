# SkyFiles Build Script (PowerShell)
Write-Host "SkyFiles Build Script" -ForegroundColor Green
Write-Host "====================" -ForegroundColor Green
Write-Host ""

Write-Host "Checking for Java installation..." -ForegroundColor Yellow

try {
    $javaVersion = java -version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Java found!" -ForegroundColor Green
        Write-Host $javaVersion[0] -ForegroundColor Cyan
        Write-Host ""
        
        Write-Host "Building the application..." -ForegroundColor Yellow
        & .\gradlew.bat build
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "Creating Windows executable..." -ForegroundColor Yellow
            & .\gradlew.bat packageReleaseExe
            
            if ($LASTEXITCODE -eq 0) {
                Write-Host ""
                Write-Host "Build complete! ✅" -ForegroundColor Green
                Write-Host "Check the build/compose/binaries/main-release/exe/ folder for SkyFiles-Setup.exe" -ForegroundColor Cyan
                
                # Check if the exe was created
                $exePath = "build\compose\binaries\main-release\exe\SkyFiles-Setup.exe"
                if (Test-Path $exePath) {
                    Write-Host ""
                    Write-Host "🎉 SkyFiles-Setup.exe created successfully!" -ForegroundColor Green
                    Write-Host "File location: $exePath" -ForegroundColor Cyan
                    Write-Host "File size: $((Get-Item $exePath).Length / 1MB) MB" -ForegroundColor Cyan
                }
            } else {
                Write-Host "Failed to create executable. Check the error messages above." -ForegroundColor Red
            }
        } else {
            Write-Host "Build failed. Check the error messages above." -ForegroundColor Red
        }
    } else {
        throw "Java not found"
    }
} catch {
    Write-Host "Java not found! Please install Java JDK 17 or higher." -ForegroundColor Red
    Write-Host ""
    Write-Host "You can download Java from:" -ForegroundColor Yellow
    Write-Host "https://adoptium.net/temurin/releases/" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "After installing Java, run this script again." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Press any key to continue..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

