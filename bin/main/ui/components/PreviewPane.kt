package skyfiles.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Preview pane for displaying file contents and metadata.
 */
@Composable
fun PreviewPane(selectedFile: File?) {
    Surface(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .padding(8.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Preview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (selectedFile == null) {
                // No file selected
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No file selected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // File metadata
                FileMetadata(selectedFile)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // File content preview
                FileContentPreview(selectedFile)
            }
        }
    }
}

@Composable
private fun FileMetadata(file: File) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "📄 ${file.name}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Size: ${formatFileSize(file.length())}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Type: ${getFileType(file)}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Modified: ${SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(file.lastModified()))}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Path: ${file.absolutePath}",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3
            )
        }
    }
}

@Composable
private fun FileContentPreview(file: File) {
    var content by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(file) {
        scope.launch(Dispatchers.IO) {
            isLoading = true
            content = try {
                when (getFileType(file).lowercase()) {
                    "image" -> "🖼️ Image file - Preview not implemented yet"
                    "video" -> "🎥 Video file - Preview not implemented yet"
                    "text" -> {
                        val lines = file.readLines()
                        if (lines.size > 100) {
                            lines.take(100).joinToString("\n") + "\n\n... (showing first 100 lines)"
                        } else {
                            lines.joinToString("\n")
                        }
                    }
                    else -> "📦 Binary file - Content preview not available"
                }
            } catch (e: Exception) {
                "❌ Error reading file: ${e.message}"
            }
            isLoading = false
        }
    }

    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Content Preview",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val scrollState = rememberScrollState()
                Text(
                    text = content ?: "No content",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                )
            }
        }
    }
}

private fun formatFileSize(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val exp = (Math.log(bytes.toDouble()) / Math.log(1024.0)).toInt()
    val pre = "KMGTPE"[exp - 1]
    return String.format("%.1f %sB", bytes / Math.pow(1024.0, exp.toDouble()), pre)
}

private fun getFileType(file: File): String {
    val extension = file.extension.lowercase()
    return when {
        file.isDirectory -> "Folder"
        extension in listOf("png", "jpg", "jpeg", "gif", "bmp", "webp") -> "Image"
        extension in listOf("mp4", "avi", "mkv", "mov", "wmv") -> "Video"
        extension in listOf("txt", "md", "log", "json", "xml", "csv") -> "Text"
        extension in listOf("zip", "rar", "7z", "tar", "gz") -> "Archive"
        extension in listOf("exe", "bat", "sh", "cmd") -> "Executable"
        else -> "File"
    }
}