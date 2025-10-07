package skyfiles.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import skyfiles.core.FileModel
import skyfiles.core.TrashManager
import java.io.File

/**
 * Context menu for file operations.
 */
@Composable
fun ContextMenu(
    file: File,
    onOpen: () -> Unit = {},
    onCopy: () -> Unit = {},
    onCut: () -> Unit = {},
    onPaste: () -> Unit = {},
    onDelete: () -> Unit = {},
    onRename: () -> Unit = {},
    onProperties: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Card(
        modifier = Modifier.width(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            if (file.isDirectory) {
                MenuItem(
                    text = "Open",
                    icon = "📁",
                    onClick = {
                        onOpen()
                        onDismiss()
                    }
                )
            } else {
                MenuItem(
                    text = "Open",
                    icon = "📄",
                    onClick = {
                        onOpen()
                        onDismiss()
                    }
                )
            }
            
            Divider()
            
            MenuItem(
                text = "Copy",
                icon = "📋",
                onClick = {
                    onCopy()
                    onDismiss()
                }
            )
            
            MenuItem(
                text = "Cut",
                icon = "✂️",
                onClick = {
                    onCut()
                    onDismiss()
                }
            )
            
            MenuItem(
                text = "Paste",
                icon = "📌",
                onClick = {
                    onPaste()
                    onDismiss()
                }
            )
            
            Divider()
            
            MenuItem(
                text = "Rename",
                icon = "✏️",
                onClick = {
                    onRename()
                    onDismiss()
                }
            )
            
            MenuItem(
                text = "Delete",
                icon = "🗑️",
                onClick = {
                    onDelete()
                    onDismiss()
                },
                textColor = MaterialTheme.colorScheme.error
            )
            
            Divider()
            
            MenuItem(
                text = "Properties",
                icon = "ℹ️",
                onClick = {
                    onProperties()
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun MenuItem(
    text: String,
    icon: String,
    onClick: () -> Unit,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            color = textColor
        )
    }
}

/**
 * Enhanced ExplorerView with context menu support.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExplorerViewWithContextMenu(
    directory: File,
    searchQuery: String,
    refreshTrigger: Int,
    onFileSelected: (File) -> Unit,
    onDirectoryChanged: (File) -> Unit,
    onFileOperation: (File, String) -> Unit = { _, _ -> }
) {
    var files by remember(refreshTrigger, directory, searchQuery) { mutableStateOf(listOf<FileModel>()) }
    var showContextMenu by remember { mutableStateOf(false) }
    var contextMenuFile by remember { mutableStateOf<File?>(null) }
    var contextMenuPosition by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(directory, refreshTrigger) {
        scope.launch(Dispatchers.IO) {
            val allFiles = directory.listFiles()?.map { FileModel.from(it) }?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() })) ?: emptyList()
            files = if (searchQuery.isBlank()) allFiles else allFiles.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Box {
        Surface(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            tonalElevation = 2.dp,
            shadowElevation = 2.dp
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(files) { fileModel ->
                    FileRowWithContextMenu(
                        fileModel = fileModel,
                        onClick = {
                            if (fileModel.isDirectory) onDirectoryChanged(File(fileModel.path))
                            else onFileSelected(File(fileModel.path))
                        },
                        onDoubleClick = {
                            if (fileModel.isDirectory) onDirectoryChanged(File(fileModel.path))
                            else onFileSelected(File(fileModel.path))
                        },
                        onRightClick = { offset ->
                            contextMenuFile = File(fileModel.path)
                            contextMenuPosition = offset
                            showContextMenu = true
                        }
                    )
                }
            }
        }

        // Context menu overlay
        if (showContextMenu && contextMenuFile != null) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.TopStart
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.offset(
                        x = contextMenuPosition.x.dp,
                        y = contextMenuPosition.y.dp
                    )
                ) {
                    ContextMenu(
                        file = contextMenuFile!!,
                        onOpen = {
                            if (contextMenuFile!!.isDirectory) {
                                onDirectoryChanged(contextMenuFile!!)
                            } else {
                                onFileSelected(contextMenuFile!!)
                            }
                        },
                        onCopy = { onFileOperation(contextMenuFile!!, "copy") },
                        onCut = { onFileOperation(contextMenuFile!!, "cut") },
                        onPaste = { onFileOperation(contextMenuFile!!, "paste") },
                        onDelete = { 
                            scope.launch(Dispatchers.IO) {
                                TrashManager.moveToTrash(contextMenuFile!!)
                            }
                        },
                        onRename = { onFileOperation(contextMenuFile!!, "rename") },
                        onProperties = { onFileOperation(contextMenuFile!!, "properties") },
                        onDismiss = { 
                            showContextMenu = false
                            contextMenuFile = null
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FileRowWithContextMenu(
    fileModel: FileModel, 
    onClick: () -> Unit, 
    onDoubleClick: () -> Unit,
    onRightClick: (androidx.compose.ui.geometry.Offset) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(44.dp)
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(fileModel.icon(), fontSize = MaterialTheme.typography.titleLarge.fontSize)
        Spacer(Modifier.width(12.dp))
        Text(fileModel.name, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(8.dp))
        Text(fileModel.formattedSize(), modifier = Modifier.width(80.dp))
        Spacer(Modifier.width(8.dp))
        Text(fileModel.formattedDate(), modifier = Modifier.width(120.dp))
    }
}

