package skyfiles.core

import java.io.File
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.*

/**
 * Data class representing a file or folder in the explorer.
 * Includes metadata for display and sorting.
 */
data class FileItem(
    val file: File,
    val name: String = file.name,
    val path: String = file.absolutePath,
    val isDirectory: Boolean = file.isDirectory,
    val size: Long = if (file.isFile) file.length() else 0L,
    val lastModified: Long = file.lastModified()
) {
    /**
     * Returns a human-readable file size (e.g., "1.2 MB").
     */
    fun getReadableSize(): String {
        if (isDirectory) return ""
        if (size < 1024) return "$size B"
        val exp = (Math.log(size.toDouble()) / Math.log(1024.0)).toInt()
        val pre = "KMGTPE"[exp - 1]
        return String.format("%.1f %sB", size / Math.pow(1024.0, exp.toDouble()), pre)
    }

    /**
     * Returns an emoji icon based on file type.
     */
    fun getFileIcon(): String {
        if (isDirectory) return "📁"
        return when (file.extension.lowercase(Locale.getDefault())) {
            "png", "jpg", "jpeg", "gif", "bmp" -> "🖼️"
            "mp4", "avi", "mov", "mkv" -> "🎬"
            "mp3", "wav", "ogg" -> "🎵"
            "pdf" -> "📄"
            "doc", "docx" -> "📝"
            "xls", "xlsx" -> "📊"
            "ppt", "pptx" -> "🖥️"
            "zip", "rar", "7z" -> "🗜️"
            "txt", "md", "log" -> "📄"
            "exe", "bat", "sh" -> "⚙️"
            else -> "📦"
        }
    }

    /**
     * Returns a formatted date string (e.g., "2023-10-27 14:30").
     */
    fun formattedDate(): String = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(lastModified))
}
