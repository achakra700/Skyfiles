package skyfiles.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import java.io.File

/**
 * Keyboard shortcuts handler for file operations.
 */
@Composable
fun KeyboardShortcuts(
    modifier: Modifier = Modifier,
    onCopy: () -> Unit = {},
    onCut: () -> Unit = {},
    onPaste: () -> Unit = {},
    onDelete: () -> Unit = {},
    onRename: () -> Unit = {},
    onSelectAll: () -> Unit = {},
    onSearch: () -> Unit = {},
    onToggleDualPane: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    onNavigateEnter: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.onKeyEvent { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                when {
                    // File operations
                    keyEvent.key == Key.C && keyEvent.isCtrlPressed -> {
                        onCopy()
                        true
                    }
                    keyEvent.key == Key.X && keyEvent.isCtrlPressed -> {
                        onCut()
                        true
                    }
                    keyEvent.key == Key.V && keyEvent.isCtrlPressed -> {
                        onPaste()
                        true
                    }
                    keyEvent.key == Key.Delete -> {
                        onDelete()
                        true
                    }
                    keyEvent.key == Key.F2 -> {
                        onRename()
                        true
                    }
                    
                    // Navigation
                    keyEvent.key == Key.Backspace -> {
                        onNavigateUp()
                        true
                    }
                    keyEvent.key == Key.Enter -> {
                        onNavigateEnter()
                        true
                    }
                    
                    // UI controls
                    keyEvent.key == Key.A && keyEvent.isCtrlPressed -> {
                        onSelectAll()
                        true
                    }
                    keyEvent.key == Key.F && keyEvent.isCtrlPressed -> {
                        onSearch()
                        true
                    }
                    keyEvent.key == Key.T && keyEvent.isCtrlPressed -> {
                        onToggleDualPane()
                        true
                    }
                    keyEvent.key == Key.S && keyEvent.isCtrlPressed -> {
                        onOpenSettings()
                        true
                    }
                    
                    else -> false
                }
            } else {
                false
            }
        }
    ) {
        content()
    }
}

/**
 * Helper function to check if Ctrl key is pressed.
 */
private fun KeyEvent.isCtrlPressed(): Boolean {
    return this.isCtrlPressed
}