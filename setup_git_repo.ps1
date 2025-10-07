# SkyFiles Git Repository Setup Script (PowerShell)
# Made with ❤️ in India 🇮🇳

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🚀 SkyFiles Git Repository Setup 🇮🇳" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Check if Git is installed
Write-Host "[1/9] Checking Git installation..." -ForegroundColor Green
try {
    $gitVersion = git --version 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Git is installed: $gitVersion" -ForegroundColor Green
    } else {
        throw "Git not found"
    }
} catch {
    Write-Host "❌ Git is not installed. Please install Git first." -ForegroundColor Red
    Write-Host "Download from: https://git-scm.com/download/win" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Step 2: Initialize Git repository if it doesn't exist
Write-Host ""
Write-Host "[2/9] Initializing Git repository..." -ForegroundColor Green
if (-not (Test-Path ".git")) {
    git init
    Write-Host "✅ Git repository initialized" -ForegroundColor Green
} else {
    Write-Host "✅ Git repository already exists" -ForegroundColor Green
}

# Step 3: Create .gitignore file
Write-Host ""
Write-Host "[3/9] Creating .gitignore file..." -ForegroundColor Green
$gitignoreContent = @"
# Kotlin Compose Desktop .gitignore
# Made with ❤️ in India 🇮🇳

# Gradle
.gradle/
build/
!gradle/wrapper/gradle-wrapper.jar
!**/src/main/**/build/
!**/src/test/**/build/

# Kotlin
*.kotlin_module
*.kotlin_metadata
*.kotlin_builtins

# Compose Desktop
compose.desktop/

# IDE
.idea/
*.iml
*.ipr
*.iws
.vscode/
.settings/
.project
.classpath

# OS
.DS_Store
Thumbs.db
Desktop.ini

# Windows
*.exe
*.msi
*.dmg

# Logs
*.log
logs/

# Temporary files
*.tmp
*.temp
*.swp
*.swo
*~

# Application data
appdata/
userdata/

# Keep important files
!README.md
!RELEASE_NOTES.md
!docs/
!build_skyfiles.bat
!build_skyfiles.ps1
!INSTALLATION_GUIDE.md
"@

$gitignoreContent | Out-File -FilePath ".gitignore" -Encoding UTF8
Write-Host "✅ .gitignore created" -ForegroundColor Green

# Step 4: Add all files to Git
Write-Host ""
Write-Host "[4/9] Adding files to Git..." -ForegroundColor Green
git add .
Write-Host "✅ Files added to Git" -ForegroundColor Green

# Step 5: Check if there are changes to commit
Write-Host ""
Write-Host "[5/9] Checking for changes..." -ForegroundColor Green
$changes = git diff --cached --name-only
if ($changes) {
    Write-Host "✅ Changes detected, ready to commit" -ForegroundColor Green
    Write-Host "Files to commit:" -ForegroundColor Yellow
    $changes | ForEach-Object { Write-Host "  - $_" -ForegroundColor Gray }
} else {
    Write-Host "ℹ️ No changes to commit" -ForegroundColor Yellow
}

# Step 6: Make initial commit
Write-Host ""
Write-Host "[6/9] Making initial commit..." -ForegroundColor Green
$commitMessage = @"
Initial commit: SkyFiles project setup with modules and docs

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
Platform: Windows
"@

git commit -m $commitMessage
Write-Host "✅ Initial commit created" -ForegroundColor Green

# Step 7: Check GitHub CLI
Write-Host ""
Write-Host "[7/9] Checking GitHub CLI..." -ForegroundColor Green
try {
    $ghVersion = gh --version 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ GitHub CLI is installed: $ghVersion" -ForegroundColor Green
        
        # Step 8: Create GitHub repository
        Write-Host ""
        Write-Host "[8/9] Creating GitHub repository..." -ForegroundColor Green
        $repoDescription = "🚀 SkyFiles - Advanced File Explorer for Windows. Made with ❤️ in India 🇮🇳"
        
        gh repo create SkyFiles --public --description $repoDescription --source=. --remote=origin --push
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ GitHub repository created and pushed" -ForegroundColor Green
            $success = $true
        } else {
            throw "Failed to create repository"
        }
    } else {
        throw "GitHub CLI not found"
    }
} catch {
    Write-Host "⚠️ GitHub CLI not found or failed. Manual setup required." -ForegroundColor Yellow
    $success = $false
}

# Step 9: Manual setup if needed
if (-not $success) {
    Write-Host ""
    Write-Host "[9/9] Manual GitHub setup required..." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "📋 Manual steps:" -ForegroundColor Cyan
    Write-Host "1. Go to https://github.com/new" -ForegroundColor White
    Write-Host "2. Repository name: SkyFiles" -ForegroundColor White
    Write-Host "3. Description: 🚀 SkyFiles - Advanced File Explorer for Windows. Made with ❤️ in India 🇮🇳" -ForegroundColor White
    Write-Host "4. Make it public" -ForegroundColor White
    Write-Host "5. Don't initialize with README (we already have one)" -ForegroundColor White
    Write-Host "6. Click 'Create repository'" -ForegroundColor White
    Write-Host ""
    Write-Host "Then run these commands:" -ForegroundColor Cyan
    Write-Host "  git remote add origin https://github.com/YOUR_USERNAME/SkyFiles.git" -ForegroundColor White
    Write-Host "  git branch -M main" -ForegroundColor White
    Write-Host "  git push -u origin main" -ForegroundColor White
}

# Success message
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🎉 SkyFiles Git Setup Complete! 🇮🇳" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📁 Repository: $(Get-Location)" -ForegroundColor Green
Write-Host "🌐 GitHub: https://github.com/YOUR_USERNAME/SkyFiles" -ForegroundColor Blue
Write-Host ""
Write-Host "🔄 Future commands:" -ForegroundColor Cyan
Write-Host "  git add ." -ForegroundColor White
Write-Host "  git commit -m 'Your commit message'" -ForegroundColor White
Write-Host "  git push" -ForegroundColor White
Write-Host ""
Write-Host "🌿 Branching:" -ForegroundColor Cyan
Write-Host "  git checkout -b feature/new-feature" -ForegroundColor White
Write-Host "  git checkout -b bugfix/fix-name" -ForegroundColor White
Write-Host ""
Write-Host "📊 Status check:" -ForegroundColor Cyan
Write-Host "  git status" -ForegroundColor White
Write-Host "  git log --oneline" -ForegroundColor White
Write-Host ""

Read-Host "Press Enter to continue"
