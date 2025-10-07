package skyfiles.core

import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.*

/**
 * Data class representing a file or folder in the explorer.
 */
data class FileModel(
    val file: File,
    val isDirectory: Boolean = file.isDirectory,
    val name: String = file.name,
    val path: String = file.absolutePath,
    val size: Long = if (file.isFile) file.length() else 0L,
    val lastModified: Long = file.lastModified(),
    val extension: String = file.extension.lowercase(Locale.getDefault())
) {
    fun formattedSize(): String = if (isDirectory) "-" else humanReadableByteCount(size)
    fun formattedDate(): String = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(lastModified))
    fun icon(): String = when {
        isDirectory -> "📁"
        extension in listOf("png", "jpg", "jpeg", "gif", "bmp") -> "🖼️"
        extension in listOf("txt", "md", "log", "json", "xml") -> "📄"
        extension in listOf("zip", "rar", "7z") -> "🗜️"
        extension in listOf("exe", "bat", "sh") -> "⚙️"
        else -> "📦"
    }
    companion object {
        fun from(file: File) = FileModel(file)
    }
}

fun humanReadableByteCount(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val exp = (Math.log(bytes.toDouble()) / Math.log(1024.0)).toInt()
    val pre = "KMGTPE"[exp - 1]
    return String.format("%.1f %sB", bytes / Math.pow(1024.0, exp.toDouble()), pre)
}
