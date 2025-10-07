package skyfiles.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import java.io.File

/**
 * Dual-pane explorer for side-by-side file management.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DualPaneExplorer(
    leftDir: File,
    rightDir: File,
    onLeftDirChanged: (File) -> Unit,
    onRightDirChanged: (File) -> Unit,
    searchQuery: String,
    refreshTrigger: Int,
    onFileSelected: (File) -> Unit
) {
    var leftFiles by remember(refreshTrigger, leftDir, searchQuery) { mutableStateOf(listOf<FileModel>()) }
    var rightFiles by remember(refreshTrigger, rightDir, searchQuery) { mutableStateOf(listOf<FileModel>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(leftDir, refreshTrigger) {
        scope.launch(Dispatchers.IO) {
            val allFiles = leftDir.listFiles()?.map { FileModel.from(it) }?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() })) ?: emptyList()
            leftFiles = if (searchQuery.isBlank()) allFiles else allFiles.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    LaunchedEffect(rightDir, refreshTrigger) {
        scope.launch(Dispatchers.IO) {
            val allFiles = rightDir.listFiles()?.map { FileModel.from(it) }?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() })) ?: emptyList()
            rightFiles = if (searchQuery.isBlank()) allFiles else allFiles.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Row(Modifier.fillMaxSize()) {
        // Left pane
        Column(Modifier.weight(1f).fillMaxHeight().padding(end = 4.dp)) {
            Text(
                text = "Left: ${leftDir.name}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp)
            )
            Surface(
                modifier = Modifier.fillMaxSize(),
                tonalElevation = 2.dp,
                shadowElevation = 2.dp
            ) {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(leftFiles) { fileModel ->
                        FileRow(
                            fileModel = fileModel,
                            onClick = {
                                if (fileModel.isDirectory) onLeftDirChanged(File(fileModel.path))
                                else onFileSelected(File(fileModel.path))
                            },
                            onDoubleClick = {
                                if (fileModel.isDirectory) onLeftDirChanged(File(fileModel.path))
                                else onFileSelected(File(fileModel.path))
                            }
                        )
                    }
                }
            }
        }

        // Divider
        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(),
            color = MaterialTheme.colorScheme.outline
        )

        // Right pane
        Column(Modifier.weight(1f).fillMaxHeight().padding(start = 4.dp)) {
            Text(
                text = "Right: ${rightDir.name}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp)
            )
            Surface(
                modifier = Modifier.fillMaxSize(),
                tonalElevation = 2.dp,
                shadowElevation = 2.dp
            ) {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(rightFiles) { fileModel ->
                        FileRow(
                            fileModel = fileModel,
                            onClick = {
                                if (fileModel.isDirectory) onRightDirChanged(File(fileModel.path))
                                else onFileSelected(File(fileModel.path))
                            },
                            onDoubleClick = {
                                if (fileModel.isDirectory) onRightDirChanged(File(fileModel.path))
                                else onFileSelected(File(fileModel.path))
                            }
                        )
                    }
                }
            }
        }
    }
}

