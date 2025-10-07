# SkyFiles - Installation & Build Guide 🇮🇳

## Quick Start (If you have Java installed)

1. **Run the build script:**
   ```bash
   .\build_skyfiles.bat
   ```
   or
   ```powershell
   .\build_skyfiles.ps1
   ```

2. **Find your executable:**
   - Look in: `build/compose/binaries/main-release/exe/SkyFiles-Setup.exe`
   - This is your installable Windows application!

## If Java is NOT installed

### Step 1: Install Java JDK 17+

**Option A: Download from Adoptium (Recommended)**
1. Go to: https://adoptium.net/temurin/releases/
2. Download "JDK 17" for Windows x64
3. Run the installer
4. Make sure to check "Add to PATH" during installation

**Option B: Download from Oracle**
1. Go to: https://www.oracle.com/java/technologies/downloads/
2. Download JDK 17 for Windows
3. Install and add to PATH

### Step 2: Verify Java Installation
Open Command Prompt or PowerShell and run:
```bash
java -version
```
You should see something like:
```
openjdk version "17.0.x" 2023-xx-xx
```

### Step 3: Build SkyFiles
```bash
.\build_skyfiles.bat
```

## What You'll Get

After successful build, you'll have:
- **SkyFiles-Setup.exe** - Windows installer
- **SkyFiles.exe** - Standalone executable
- **Complete Windows application** with all features

## Features Included

✅ **Single & Dual-Pane File Explorer**
✅ **File Previews** (Images, Videos, Text)
✅ **Trash/Recycle Bin System**
✅ **Keyboard Shortcuts** (Ctrl+C/X/V, F2, Delete, etc.)
✅ **Context Menus** (Right-click operations)
✅ **Settings Window** (Theme, Auto-updates)
✅ **Splash Screen** with Indian touch 🇮🇳
✅ **Auto-Update System**
✅ **Material 3 Theme Support**

## Troubleshooting

**"Java not found" error:**
- Install Java JDK 17+ and add to PATH
- Restart Command Prompt/PowerShell after installation

**Build fails:**
- Check internet connection (Gradle downloads dependencies)
- Ensure you have write permissions in the project folder
- Try running as Administrator

**Executable not created:**
- Check the `build/compose/binaries/main-release/exe/` folder
- Look for any error messages during the build process

## Manual Build Commands

If the scripts don't work, try these commands manually:

```bash
# Build the project
.\gradlew.bat build

# Create Windows executable
.\gradlew.bat packageReleaseExe

# Run the app directly (for testing)
.\gradlew.bat run
```

## File Locations

- **Source Code**: `src/main/kotlin/`
- **Resources**: `src/main/resources/`
- **Documentation**: `docs/`
- **Built Executable**: `build/compose/binaries/main-release/exe/`

---

**Made with ❤️ in India 🇮🇳**

Once built, you'll have a complete, professional Windows file explorer that rivals commercial alternatives!

