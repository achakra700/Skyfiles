@echo off
REM SkyFiles Git Repository Setup Script
REM Made with ❤️ in India 🇮🇳

echo.
echo ========================================
echo 🚀 SkyFiles Git Repository Setup 🇮🇳
echo ========================================
echo.

REM Step 1: Check if Git is installed
echo [1/9] Checking Git installation...
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Git is not installed. Please install Git first.
    echo Download from: https://git-scm.com/download/win
    pause
    exit /b 1
)
echo ✅ Git is installed

REM Step 2: Initialize Git repository if it doesn't exist
echo.
echo [2/9] Initializing Git repository...
if not exist ".git" (
    git init
    echo ✅ Git repository initialized
) else (
    echo ✅ Git repository already exists
)

REM Step 3: Create .gitignore file
echo.
echo [3/9] Creating .gitignore file...
(
echo # Kotlin Compose Desktop .gitignore
echo # Made with ❤️ in India 🇮🇳
echo.
echo # Gradle
echo .gradle/
echo build/
echo !gradle/wrapper/gradle-wrapper.jar
echo !**/src/main/**/build/
echo !**/src/test/**/build/
echo.
echo # Kotlin
echo *.kotlin_module
echo *.kotlin_metadata
echo *.kotlin_builtins
echo.
echo # Compose Desktop
echo compose.desktop/
echo.
echo # IDE
echo .idea/
echo *.iml
echo *.ipr
echo *.iws
echo .vscode/
echo .settings/
echo .project
echo .classpath
echo.
echo # OS
echo .DS_Store
echo Thumbs.db
echo Desktop.ini
echo.
echo # Windows
echo *.exe
echo *.msi
echo *.dmg
echo.
echo # Logs
echo *.log
echo logs/
echo.
echo # Temporary files
echo *.tmp
echo *.temp
echo *.swp
echo *.swo
echo *~
echo.
echo # Application data
echo appdata/
echo userdata/
echo.
echo # Keep important files
echo !README.md
echo !RELEASE_NOTES.md
echo !docs/
echo !build_skyfiles.bat
echo !build_skyfiles.ps1
echo !INSTALLATION_GUIDE.md
) > .gitignore
echo ✅ .gitignore created

REM Step 4: Add all files to Git
echo.
echo [4/9] Adding files to Git...
git add .
echo ✅ Files added to Git

REM Step 5: Check if there are changes to commit
echo.
echo [5/9] Checking for changes...
git diff --cached --quiet
if %errorlevel% equ 0 (
    echo ℹ️ No changes to commit
) else (
    echo ✅ Changes detected, ready to commit
)

REM Step 6: Make initial commit
echo.
echo [6/9] Making initial commit...
git commit -m "Initial commit: SkyFiles project setup with modules and docs

🚀 SkyFiles - Advanced File Explorer for Windows
🇮🇳 Made with ❤️ in India

Features included:
- Single & Dual-pane file explorer
- File previews (images, videos, text)
- Trash/Recycle Bin system
- Keyboard shortcuts & context menus
- Settings window with theme support
- Splash screen with Indian touch
- Auto-update system
- Material 3 UI design
- Complete documentation

Version: 1.0.1
Build: Kotlin Compose Desktop
Platform: Windows"
echo ✅ Initial commit created

REM Step 7: Check GitHub CLI
echo.
echo [7/9] Checking GitHub CLI...
gh --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ⚠️ GitHub CLI not found. You'll need to create repository manually.
    echo Download from: https://cli.github.com/
    goto :manual_setup
)

REM Step 8: Create GitHub repository
echo.
echo [8/9] Creating GitHub repository...
gh repo create SkyFiles --public --description "🚀 SkyFiles - Advanced File Explorer for Windows. Made with ❤️ in India 🇮🇳" --source=. --remote=origin --push
if %errorlevel% equ 0 (
    echo ✅ GitHub repository created and pushed
    goto :success
) else (
    echo ⚠️ Failed to create GitHub repository automatically
    goto :manual_setup
)

:manual_setup
echo.
echo [9/9] Manual GitHub setup required...
echo.
echo 📋 Manual steps:
echo 1. Go to https://github.com/new
echo 2. Repository name: SkyFiles
echo 3. Description: 🚀 SkyFiles - Advanced File Explorer for Windows. Made with ❤️ in India 🇮🇳
echo 4. Make it public
echo 5. Don't initialize with README (we already have one)
echo 6. Click "Create repository"
echo.
echo Then run these commands:
echo   git remote add origin https://github.com/YOUR_USERNAME/SkyFiles.git
echo   git branch -M main
echo   git push -u origin main
echo.
goto :success

:success
echo.
echo ========================================
echo 🎉 SkyFiles Git Setup Complete! 🇮🇳
echo ========================================
echo.
echo 📁 Repository: %cd%
echo 🌐 GitHub: https://github.com/YOUR_USERNAME/SkyFiles
echo.
echo 🔄 Future commands:
echo   git add .
echo   git commit -m "Your commit message"
echo   git push
echo.
echo 🌿 Branching:
echo   git checkout -b feature/new-feature
echo   git checkout -b bugfix/fix-name
echo.
echo 📊 Status check:
echo   git status
echo   git log --oneline
echo.
pause
