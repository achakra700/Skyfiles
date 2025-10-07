package skyfiles.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import skyfiles.core.FileModel
import java.io.File

/**
 * Main explorer view: lists files/folders, supports navigation and selection.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExplorerView(
    directory: File,
    searchQuery: String,
    refreshTrigger: Int,
    onFileSelected: (File) -> Unit,
    onDirectoryChanged: (File) -> Unit
) {
    var files by remember(refreshTrigger, directory, searchQuery) { mutableStateOf(listOf<FileModel>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(directory, refreshTrigger) {
        scope.launch(Dispatchers.IO) {
            val allFiles = directory.listFiles()?.map { FileModel.from(it) }?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() })) ?: emptyList()
            files = if (searchQuery.isBlank()) allFiles else allFiles.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().padding(4.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(files) { fileModel ->
                FileRow(
                    fileModel = fileModel,
                    onClick = {
                        if (fileModel.isDirectory) onDirectoryChanged(File(fileModel.path))
                        else onFileSelected(File(fileModel.path))
                    },
                    onDoubleClick = {
                        if (fileModel.isDirectory) onDirectoryChanged(File(fileModel.path))
                        else onFileSelected(File(fileModel.path))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileRow(
    fileModel: FileModel, 
    onClick: () -> Unit, 
    onDoubleClick: () -> Unit,
    isSelected: Boolean = false
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(44.dp)
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                else MaterialTheme.colorScheme.surface
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(fileModel.icon(), fontSize = MaterialTheme.typography.titleLarge.fontSize)
        Spacer(Modifier.width(12.dp))
        Text(
            fileModel.name, 
            modifier = Modifier.weight(1f),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer 
                   else MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.width(8.dp))
        Text(
            fileModel.formattedSize(), 
            modifier = Modifier.width(80.dp),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer 
                   else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(8.dp))
        Text(
            fileModel.formattedDate(), 
            modifier = Modifier.width(120.dp),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer 
                   else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
