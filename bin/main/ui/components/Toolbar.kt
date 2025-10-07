package skyfiles.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import java.io.File

/**
 * Toolbar with breadcrumbs, search bar, refresh button, and additional controls.
 */
@Composable
fun Toolbar(
    currentDir: File,
    onNavigate: (File) -> Unit,
    onRefresh: () -> Unit,
    searchQuery: String,
    onSearch: (String) -> Unit,
    isDualPane: Boolean = false,
    onToggleDualPane: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Breadcrumbs(currentDir, onNavigate)
        Spacer(Modifier.width(16.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearch,
            placeholder = { Text("Search...") },
            modifier = Modifier.weight(1f).height(40.dp)
        )
        Spacer(Modifier.width(8.dp))
        
        // Dual pane toggle
        IconButton(onClick = onToggleDualPane) {
            Text(if (isDualPane) "📊" else "📋")
        }
        
        // Refresh button
        IconButton(onClick = onRefresh) {
            Text("🔄")
        }
        
        // Settings button
        IconButton(onClick = onOpenSettings) {
            Text("⚙️")
        }
    }
}

/**
 * Breadcrumb navigation for current path.
 */
@Composable
fun Breadcrumbs(currentDir: File, onNavigate: (File) -> Unit) {
    val segments = currentDir.absolutePath.split(File.separator).filter { it.isNotEmpty() }
    Row(verticalAlignment = Alignment.CenterVertically) {
        segments.forEachIndexed { idx, segment ->
            val path = File(File.separator + segments.take(idx + 1).joinToString(File.separator))
            Text(
                text = segment,
                modifier = Modifier.clickable { onNavigate(path) }.padding(horizontal = 4.dp),
                color = MaterialTheme.colorScheme.primary
            )
            if (idx < segments.lastIndex) Text("/", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
