package skyfiles.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import skyfiles.core.TrashManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Trash view for managing deleted files.
 */
@Composable
fun TrashView(
    onRestore: (String) -> Unit = {},
    onEmptyTrash: () -> Unit = {}
) {
    var trashItems by remember { mutableStateOf<List<Pair<File, TrashManager.TrashInfo>>>(emptyList()) }
    var showEmptyDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            trashItems = TrashManager.listTrash()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🗑️ Trash",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Row {
                Button(
                    onClick = { showEmptyDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Empty Trash")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (trashItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🗑️",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No items in Trash",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Deleted files will appear here",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(trashItems) { (file, info) ->
                    TrashItemCard(
                        file = file,
                        info = info,
                        onRestore = {
                            scope.launch(Dispatchers.IO) {
                                val success = TrashManager.restoreFromTrash(file.name)
                                if (success) {
                                    trashItems = TrashManager.listTrash()
                                    onRestore(info.originalPath)
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    // Empty trash confirmation dialog
    if (showEmptyDialog) {
        // Simple confirmation - in a real app, you'd use AlertDialog
        Text("Empty Trash? (This will permanently delete all items)")
        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    trashItems.forEach { (file, _) ->
                        file.deleteRecursively()
                    }
                    trashItems = emptyList()
                    showEmptyDialog = false
                    onEmptyTrash()
                }
            }
        ) {
            Text("Delete All")
        }
        Button(onClick = { showEmptyDialog = false }) {
            Text("Cancel")
        }
    }
}

@Composable
private fun TrashItemCard(
    file: File,
    info: TrashManager.TrashInfo,
    onRestore: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (file.isDirectory) "📁" else "📄",
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Original: ${info.originalPath}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Deleted: ${SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(info.deletedAt))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Button(
                onClick = onRestore,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Restore")
            }
        }
    }
}

