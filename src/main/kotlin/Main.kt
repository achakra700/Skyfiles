package skyfiles

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.launch
import skyfiles.core.AppLogger
import skyfiles.core.SettingsManager
import skyfiles.core.UpdateChecker
import skyfiles.ui.SkyFilesApp
import java.awt.Desktop
import java.io.File

/**
 * Entry point for SkyFiles Compose Desktop application.
 */
fun main() = application {
    AppLogger.setup()

    val settings = remember { SettingsManager.loadSettings() }
    var isDarkTheme by remember {
        mutableStateOf(
            when (settings.theme) {
                "Dark" -> true
                "Light" -> false
                else -> false // Default to light theme
            }
        )
    }
    val colors = if (isDarkTheme) darkColorScheme() else lightColorScheme()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var downloadProgress by remember { mutableStateOf<Float?>(null) }
    var downloadedFile by remember { mutableStateOf<File?>(null) }

    val onDownloadRequested: (UpdateChecker.UpdateInfo) -> Unit = { updateInfo ->
        scope.launch {
            downloadProgress = 0f
            val file = UpdateChecker.downloadUpdate(updateInfo) { progress ->
                downloadProgress = progress
            }
            if (file != null) {
                downloadedFile = file
                downloadProgress = null
                val result = snackbarHostState.showSnackbar(
                    message = "Update downloaded. Click to install.",
                    actionLabel = "Install",
                    duration = SnackbarDuration.Indefinite
                )
                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    Desktop.getDesktop().open(downloadedFile)
                }
            } else {
                downloadProgress = null
                snackbarHostState.showSnackbar("Update download failed.")
            }
        }
    }

    LaunchedEffect(Unit) {
        if (settings.autoUpdate) {
            val updateInfo = UpdateChecker.checkForUpdate()
            if (updateInfo != null) {
                val result = snackbarHostState.showSnackbar(
                    message = "A new version (${updateInfo.version}) is available.",
                    actionLabel = "Download",
                    duration = SnackbarDuration.Long
                )
                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    onDownloadRequested(updateInfo)
                }
            }
        }
    }

    val windowState = rememberWindowState(
        width = settings.windowWidth.dp,
        height = settings.windowHeight.dp
    )

    Window(
        onCloseRequest = {
            SettingsManager.saveSettings(
                settings.copy(
                    windowWidth = windowState.size.width.value.toInt(),
                    windowHeight = windowState.size.height.value.toInt()
                )
            )
            exitApplication()
        },
        title = "SkyFiles – Advanced File Explorer",
        state = windowState,
        icon = painterResource("icon.ico")
    ) {
        MaterialTheme(colorScheme = colors) {
            SkyFilesApp(
                settings = settings,
                onThemeChanged = { newTheme ->
                    isDarkTheme = when (newTheme) {
                        "Dark" -> true
                        "Light" -> false
                        else -> false
                    }
                    SettingsManager.saveSettings(settings.copy(theme = newTheme))
                },
                snackbarHostState = snackbarHostState,
                downloadProgress = remember { mutableStateOf(downloadProgress) },
                onDownloadRequested = onDownloadRequested
            )
        }
    }
}

