package skyfiles.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File

/**
 * Tab bar for multiple directory tabs (bonus feature).
 */
@Composable
fun TabBar(
    tabs: List<File>,
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    onTabClosed: (Int) -> Unit,
    onNewTab: () -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = currentTab,
        edgePadding = 8.dp
    ) {
        tabs.forEachIndexed { idx, file ->
            Tab(
                selected = idx == currentTab,
                onClick = { onTabSelected(idx) },
                text = { Text(file.name.ifBlank { file.absolutePath }) },
                icon = { Text(if (file.isDirectory) "📁" else "📄") },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Tab(
            selected = false,
            onClick = onNewTab,
            text = { Text("+") }
        )
    }
}
