package skyfiles.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

private val logger = AppLogger.getLogger(FileUtils::class.java)

/**
 * Utility functions for all file system operations.
 * Uses coroutines for non-blocking I/O.
 */
object FileUtils {

    /**
     * Lists all files and directories in a given directory.
     * Returns a sorted list of FileItem objects.
     */
    suspend fun listFiles(directory: File): List<FileItem> = withContext(Dispatchers.IO) {
        try {
            if (!directory.exists() || !directory.isDirectory) {
                logger.warn("Directory does not exist or is not a directory: ${directory.absolutePath}")
                return@withContext emptyList()
            }
            directory.listFiles()
                ?.map { FileItem(it) }
                ?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
                ?: emptyList()
        } catch (e: SecurityException) {
            logger.error("Permission denied when trying to list files in ${directory.absolutePath}", e)
            emptyList()
        }
    }

    /**
     * Copies a file or directory to a destination.
     */
    suspend fun copyFile(source: File, destination: File): Unit = withContext(Dispatchers.IO) {
        try {
            if (source.isDirectory) {
                destination.mkdirs()
                source.listFiles()?.forEach { child: File ->
                    copyFile(child, File(destination, child.name))
                }
            } else {
                source.copyTo(destination, overwrite = true)
            }
            logger.info("Copied ${source.absolutePath} to ${destination.absolutePath}")
        } catch (e: IOException) {
            logger.error("Failed to copy ${source.absolutePath} to ${destination.absolutePath}", e)
            throw e
        }
    }

    /**
     * Moves a file or directory to a destination.
     */
    suspend fun moveFile(source: File, destination: File) = withContext(Dispatchers.IO) {
        try {
            copyFile(source, destination)
            deleteFile(source, isPermanent = true) // Use permanent delete for move
            logger.info("Moved ${source.absolutePath} to ${destination.absolutePath}")
        } catch (e: IOException) {
            logger.error("Failed to move ${source.absolutePath} to ${destination.absolutePath}", e)
            throw e
        }
    }

    /**
     * Deletes a file or directory. By default, it moves the item to the trash.
     * If isPermanent is true, it deletes the file permanently.
     */
    suspend fun deleteFile(file: File, isPermanent: Boolean = false) {
        if (isPermanent) {
            withContext(Dispatchers.IO) {
                try {
                    if (file.isDirectory) {
                        file.deleteRecursively()
                    } else {
                        file.delete()
                    }
                    logger.info("Permanently deleted ${file.absolutePath}")
                } catch (e: SecurityException) {
                    logger.error("Failed to permanently delete ${file.name}", e)
                    throw IOException("Failed to delete ${file.name}", e)
                }
            }
        } else {
            TrashManager.moveToTrash(file)
            logger.info("Moved ${file.absolutePath} to trash")
        }
    }

    /**
     * Renames a file or directory.
     */
    suspend fun renameFile(file: File, newName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val newFile = File(file.parent, newName)
            val success = file.renameTo(newFile)
            if (success) {
                logger.info("Renamed ${file.absolutePath} to $newName")
            } else {
                logger.warn("Failed to rename ${file.absolutePath} to $newName")
            }
            success
        } catch (e: SecurityException) {
            logger.error("Permission denied when trying to rename ${file.absolutePath}", e)
            false
        }
    }
}
