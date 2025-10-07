package skyfiles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File

private val quickAccess = listOf(
    "Home" to System.getProperty("user.home"),
    "Desktop" to (System.getProperty("user.home") + "/Desktop"),
    "Documents" to (System.getProperty("user.home") + "/Documents"),
    "Downloads" to (System.getProperty("user.home") + "/Downloads")
)

/**
 * Sidebar for quick access locations and trash.
 */
@Composable
fun Sidebar(
    currentDir: File, 
    onNavigate: (File) -> Unit,
    onOpenTrash: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.width(180.dp).fillMaxHeight(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(Modifier.padding(top = 16.dp)) {
            // Quick access locations
            quickAccess.forEach { (label, path) ->
                val file = File(path)
                val isSelected = currentDir.absolutePath == file.absolutePath
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(file) }
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(if (file.isDirectory) "📁" else "📄")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Trash
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenTrash() }
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text("🗑️")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Trash")
            }
        }
    }
}
