package skyfiles.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class AppSettings(
    val theme: String = "System",
    val autoUpdate: Boolean = true,
    val windowWidth: Int = 1200,
    val windowHeight: Int = 800,
    val windowX: Int = 100,
    val windowY: Int = 100
)

object SettingsManager {
    private val settingsFile = File(System.getenv("APPDATA") + "/SkyFiles/settings.json")
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    fun loadSettings(): AppSettings {
        return if (settingsFile.exists()) {
            try {
                json.decodeFromString(AppSettings.serializer(), settingsFile.readText())
            } catch (e: Exception) {
                AppLogger.getLogger(SettingsManager::class.java).error("Failed to load settings", e)
                AppSettings()
            }
        } else {
            AppSettings()
        }
    }

    fun saveSettings(settings: AppSettings) {
        try {
            settingsFile.parentFile?.mkdirs()
            settingsFile.writeText(json.encodeToString(AppSettings.serializer(), settings))
        } catch (e: Exception) {
            AppLogger.getLogger(SettingsManager::class.java).error("Failed to save settings", e)
        }
    }
}
