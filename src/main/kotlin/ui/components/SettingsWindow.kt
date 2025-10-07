package skyfiles.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import skyfiles.core.AppSettings

/**
 * Settings window for configuring app preferences.
 */
@Composable
fun SettingsWindow(
    settings: AppSettings,
    onSettingsChanged: (AppSettings) -> Unit,
    onDismiss: () -> Unit
) {
    var currentSettings by remember { mutableStateOf(settings) }
    var showFolderPicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(500.dp)
                .height(400.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "⚙️ Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Theme selection
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                val themes = listOf("System", "Light", "Dark")
                themes.forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = currentSettings.theme == theme,
                                onClick = { currentSettings = currentSettings.copy(theme = theme) }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSettings.theme == theme,
                            onClick = { currentSettings = currentSettings.copy(theme = theme) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = theme)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Auto-update toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Check for updates automatically",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = currentSettings.autoUpdate,
                        onCheckedChange = { 
                            currentSettings = currentSettings.copy(autoUpdate = it)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSettingsChanged(currentSettings)
                            onDismiss()
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

