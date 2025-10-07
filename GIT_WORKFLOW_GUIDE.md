# SkyFiles Git Workflow Guide 🇮🇳
# Made with ❤️ in India

## 🚀 Quick Start Commands

### Option 1: Automated Setup (Recommended)
```bash
# Windows Command Prompt
setup_git_repo.bat

# PowerShell
.\setup_git_repo.ps1
```

### Option 2: Manual Setup
```bash
# 1. Initialize Git repository
git init

# 2. Create .gitignore (already created by script)
# The .gitignore file is automatically created with proper Kotlin/Gradle exclusions

# 3. Add all files
git add .

# 4. Make initial commit
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

# 5. Create GitHub repository (if GitHub CLI available)
gh repo create SkyFiles --public --description "🚀 SkyFiles - Advanced File Explorer for Windows. Made with ❤️ in India 🇮🇳" --source=. --remote=origin --push

# 6. Or manually create repository and link
git remote add origin https://github.com/YOUR_USERNAME/SkyFiles.git
git branch -M main
git push -u origin main
```

## 📁 Files Included in Repository

### Source Code
- `src/main/kotlin/` - Complete Kotlin source code
- `src/main/resources/` - Icons and resources
- `build.gradle.kts` - Gradle build configuration

### Documentation
- `docs/README.md` - Comprehensive user guide
- `docs/RELEASE_NOTES.md` - Version history and features
- `INSTALLATION_GUIDE.md` - Installation instructions

### Build Scripts
- `build_skyfiles.bat` - Windows batch build script
- `build_skyfiles.ps1` - PowerShell build script
- `setup_git_repo.bat` - Git setup automation
- `setup_git_repo.ps1` - PowerShell Git setup

### Configuration
- `.gitignore` - Proper exclusions for Kotlin/Gradle projects

## 🔄 Future Git Commands

### Daily Development
```bash
# Check status
git status

# Add changes
git add .

# Commit with message
git commit -m "Add new feature: drag and drop support"

# Push to remote
git push
```

### Branching Strategy
```bash
# Create feature branch
git checkout -b feature/drag-drop

# Create bugfix branch
git checkout -b bugfix/trash-restore-fix

# Create release branch
git checkout -b release/v1.0.2

# Switch back to main
git checkout main

# Merge feature branch
git merge feature/drag-drop
```

### Release Management
```bash
# Tag releases
git tag -a v1.0.1 -m "Release version 1.0.1"
git push origin v1.0.1

# List tags
git tag -l

# Checkout specific version
git checkout v1.0.1
```

## 🛠️ Error Handling

### Common Issues & Solutions

#### 1. Git Not Installed
```bash
# Download Git from: https://git-scm.com/download/win
# Or use package manager:
winget install Git.Git
```

#### 2. GitHub CLI Not Available
```bash
# Install GitHub CLI:
winget install GitHub.cli
# Or download from: https://cli.github.com/
```

#### 3. Repository Already Exists
```bash
# If repository already exists locally:
git remote -v  # Check existing remotes
git remote remove origin  # Remove if needed
git remote add origin https://github.com/YOUR_USERNAME/SkyFiles.git
```

#### 4. Authentication Issues
```bash
# Set up GitHub authentication:
gh auth login
# Or use personal access token
git config --global credential.helper store
```

#### 5. Large Files
```bash
# If build artifacts are too large:
echo "build/" >> .gitignore
echo "*.exe" >> .gitignore
git rm --cached build/ -r
git commit -m "Remove build artifacts from tracking"
```

## 📊 Repository Structure

```
SkyFiles/
├── .git/                    # Git repository data
├── .gitignore              # Git exclusions
├── src/                    # Source code
│   ├── main/kotlin/        # Kotlin source files
│   └── main/resources/     # Icons and resources
├── docs/                   # Documentation
│   ├── README.md          # User guide
│   └── RELEASE_NOTES.md   # Version history
├── build.gradle.kts       # Build configuration
├── build_skyfiles.bat     # Build script
├── build_skyfiles.ps1     # PowerShell build script
├── setup_git_repo.bat     # Git setup automation
├── setup_git_repo.ps1     # PowerShell Git setup
└── INSTALLATION_GUIDE.md  # Installation guide
```

## 🎯 Best Practices

### Commit Messages
- Use descriptive messages
- Include emoji for visual clarity
- Reference issues/features
- Keep under 72 characters for subject line

### Branch Naming
- `feature/feature-name` - New features
- `bugfix/issue-description` - Bug fixes
- `hotfix/critical-fix` - Critical fixes
- `release/version-number` - Release preparation

### File Organization
- Keep documentation up to date
- Include build scripts
- Maintain proper .gitignore
- Version all dependencies

## 🇮🇳 Indian Touch Elements

The repository includes:
- 🇮🇳 Indian flag emojis in descriptions
- "Made with ❤️ in India" taglines
- Cultural references in documentation
- Indian-themed commit messages

## 🚀 Next Steps After Setup

1. **Verify Repository**: Check GitHub repository is created
2. **Test Build**: Run build scripts to ensure everything works
3. **Documentation**: Update README with repository links
4. **Issues**: Create GitHub issues for future features
5. **Releases**: Set up GitHub releases for version management
6. **CI/CD**: Consider GitHub Actions for automated builds

---

**Made with ❤️ in India** 🇮🇳  
**SkyFiles - Advanced File Explorer for Windows**
