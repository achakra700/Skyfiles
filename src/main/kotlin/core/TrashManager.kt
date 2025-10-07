package skyfiles.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object TrashManager {
    private val trashDir = File(System.getenv("APPDATA") + "/SkyFiles/Trash")
    private val infoDir = File(trashDir, "info")
    private val json = Json { prettyPrint = true }

    init {
        trashDir.mkdirs()
        infoDir.mkdirs()
    }

    @Serializable
    data class TrashInfo(val originalPath: String, val deletedAt: Long)

    fun moveToTrash(file: File): Boolean {
        return try {
            val trashedFile = File(trashDir, file.name)
            Files.move(file.toPath(), trashedFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            val info = TrashInfo(file.absolutePath, System.currentTimeMillis())
            File(infoDir, file.name + ".json").writeText(json.encodeToString(TrashInfo.serializer(), info))
            true
        } catch (e: Exception) {
            AppLogger.getLogger(TrashManager::class.java).error("Failed to move to trash", e)
            false
        }
    }

    fun restoreFromTrash(fileName: String): Boolean {
        val trashedFile = File(trashDir, fileName)
        val infoFile = File(infoDir, fileName + ".json")
        return if (trashedFile.exists() && infoFile.exists()) {
            try {
                val info = json.decodeFromString(TrashInfo.serializer(), infoFile.readText())
                Files.move(trashedFile.toPath(), File(info.originalPath).toPath(), StandardCopyOption.REPLACE_EXISTING)
                infoFile.delete()
                true
            } catch (e: Exception) {
                AppLogger.getLogger(TrashManager::class.java).error("Failed to restore from trash", e)
                false
            }
        } else false
    }

    fun listTrash(): List<Pair<File, TrashInfo>> {
        return infoDir.listFiles { f -> f.extension == "json" }?.mapNotNull { infoFile ->
            try {
                val info = json.decodeFromString(TrashInfo.serializer(), infoFile.readText())
                val trashedFile = File(trashDir, infoFile.nameWithoutExtension)
                if (trashedFile.exists()) Pair(trashedFile, info) else null
            } catch (_: Exception) {
                null
            }
        } ?: emptyList()
    }
}
