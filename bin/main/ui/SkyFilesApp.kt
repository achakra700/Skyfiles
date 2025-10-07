package skyfiles.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import skyfiles.core.AppSettings
import skyfiles.core.UpdateChecker
import skyfiles.ui.components.*
import java.io.File

/**
 * Main app composable. Sets up the layout and state.
 */
@Composable
fun SkyFilesApp(
    settings: AppSettings,
    onThemeChanged: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    downloadProgress: MutableState<Float?>,
    onDownloadRequested: (UpdateChecker.UpdateInfo) -> Unit
) {
    val homeDir = System.getProperty("user.home")
    var currentDir by remember { mutableStateOf(File(homeDir)) }
    var selectedFile by remember { mutableStateOf<File?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var refreshTrigger by remember { mutableStateOf(0) }
    var isDualPane by remember { mutableStateOf(false) }
    var showSplash by remember { mutableStateOf(true) }

    var showSettings by remember { mutableStateOf(false) }
    var showTrash by remember { mutableStateOf(false) }
    var rightDir by remember { mutableStateOf(currentDir) }
    var selectedFiles by remember { mutableStateOf<List<File>>(emptyList()) }
    var copiedFiles by remember { mutableStateOf<List<File>>(emptyList()) }
    var isCutOperation by remember { mutableStateOf(false) }

    // Show splash screen initially
    if (showSplash) {
        SplashScreen(
            onComplete = { showSplash = false }
        )
    } else {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                KeyboardShortcuts(
                    onCopy = { 
                        if (selectedFile != null) {
                            copiedFiles = listOf(selectedFile!!)
                            isCutOperation = false
                        }
                    },
                    onCut = { 
                        if (selectedFile != null) {
                            copiedFiles = listOf(selectedFile!!)
                            isCutOperation = true
                        }
                    },
                    onPaste = { 
                        copiedFiles.forEach { file ->
                            try {
                                val destFile = File(currentDir, file.name)
                                if (isCutOperation) {
                                    file.renameTo(destFile)
                                } else {
                                    file.copyTo(destFile, overwrite = true)
                                }
                                refreshTrigger++
                            } catch (e: Exception) {
                                // Handle error silently for now
                            }
                        }
                        copiedFiles = emptyList()
                        isCutOperation = false
                    },
                    onDelete = { 
                        selectedFile?.let { file ->
                            skyfiles.core.TrashManager.moveToTrash(file)
                            refreshTrigger++
                            selectedFile = null
                        }
                    },
                    onRename = { 
                        // TODO: Implement rename dialog
                    },
                    onSelectAll = { 
                        // TODO: Implement select all
                    },
                    onSearch = { 
                        // TODO: Focus search bar
                    },
                    onToggleDualPane = { isDualPane = !isDualPane },
                    onOpenSettings = { showSettings = true },
                    onNavigateUp = { 
                        currentDir.parentFile?.let { currentDir = it }
                        selectedFile = null
                    },
                    onNavigateEnter = { 
                        selectedFile?.let { file ->
                            if (file.isDirectory) {
                                currentDir = file
                                selectedFile = null
                            }
                        }
                    }
                ) {
                    Row(Modifier.fillMaxSize()) {
                        Sidebar(
                            currentDir = currentDir,
                            onNavigate = { dir ->
                                currentDir = dir
                                selectedFile = null
                            },
                            onOpenTrash = { showTrash = true }
                        )
                        Column(Modifier.weight(1f).fillMaxHeight().padding(8.dp)) {
                            Toolbar(
                                currentDir = currentDir,
                                onNavigate = { currentDir = it; selectedFile = null },
                                onRefresh = { refreshTrigger++ },
                                searchQuery = searchQuery,
                                onSearch = { searchQuery = it },
                                isDualPane = isDualPane,
                                onToggleDualPane = { isDualPane = !isDualPane },
                                onOpenSettings = { showSettings = true }
                            )
                            Spacer(Modifier.height(8.dp))
                            
                            if (isDualPane) {
                                DualPaneExplorer(
                                    leftDir = currentDir,
                                    rightDir = rightDir,
                                    onLeftDirChanged = { currentDir = it },
                                    onRightDirChanged = { rightDir = it },
                                    searchQuery = searchQuery,
                                    refreshTrigger = refreshTrigger,
                                    onFileSelected = { selectedFile = it }
                                )
                            } else {
                                ExplorerView(
                                    directory = currentDir,
                                    searchQuery = searchQuery,
                                    refreshTrigger = refreshTrigger,
                                    onFileSelected = { selectedFile = it },
                                    onDirectoryChanged = { currentDir = it; selectedFile = null }
                                )
                            }
                        }
                        PreviewPane(selectedFile)
                    }
                }
            }
        }
    }

    // Settings dialog
    if (showSettings) {
        SettingsWindow(
            settings = settings,
            onSettingsChanged = { newSettings ->
                onThemeChanged(newSettings.theme)
            },
            onDismiss = { showSettings = false }
        )
    }

    // Trash dialog - simplified
    if (showTrash) {
        // Simple text-based trash view for now
        Text("Trash functionality available in sidebar")
    }
}
