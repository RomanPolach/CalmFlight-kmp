package com.romanpolach.peacefulflight.kmp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.data.preferences.SettingsRepository
import com.romanpolach.peacefulflight.kmp.data.preferences.UnitSystem
import com.romanpolach.peacefulflight.kmp.model.ThemeMode
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.settings

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit
) {
    val settingsRepository: SettingsRepository = koinInject()

    var selectedUnitSystem by remember {
        mutableStateOf(settingsRepository.getUnitSystem())
    }

    var selectedTheme by remember {
        mutableStateOf(settingsRepository.getThemeMode())
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.settings),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Unit System",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectableGroup(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SettingsOption(
                            title = "Metric",
                            description = "Celsius (\u00B0C), km/h",
                            selected = selectedUnitSystem == UnitSystem.METRIC,
                            onClick = { selectedUnitSystem = UnitSystem.METRIC }
                        )

                        SettingsOption(
                            title = "Imperial",
                            description = "Fahrenheit (\u00B0F), mph",
                            selected = selectedUnitSystem == UnitSystem.IMPERIAL,
                            onClick = { selectedUnitSystem = UnitSystem.IMPERIAL }
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Theme",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectableGroup(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SettingsOption(
                            title = "System Default",
                            description = "Follow system settings",
                            selected = selectedTheme == ThemeMode.SYSTEM,
                            onClick = { selectedTheme = ThemeMode.SYSTEM }
                        )

                        SettingsOption(
                            title = "Light Mode",
                            description = "Always light theme",
                            selected = selectedTheme == ThemeMode.LIGHT,
                            onClick = { selectedTheme = ThemeMode.LIGHT }
                        )

                        SettingsOption(
                            title = "Dark Mode",
                            description = "Always dark theme",
                            selected = selectedTheme == ThemeMode.DARK,
                            onClick = { selectedTheme = ThemeMode.DARK }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    settingsRepository.setUnitSystem(selectedUnitSystem)
                    settingsRepository.setThemeMode(selectedTheme)
                    onDismiss()
                }
            ) {
                Text("Save", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun SettingsOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        shape = RoundedCornerShape(8.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
