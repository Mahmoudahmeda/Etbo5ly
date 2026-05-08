package com.example.etbo5ly.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AboutDialog(
    onDismiss: () -> Unit,
    appVersion: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface, // Uses dynamic surface color
        title = {
            Text(
                text = "About Etbo5ly", 
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = "Etbo5ly is your personal meal planning companion.\n\n" +
                        "Version: $appVersion\n" +
                        "Developed by the Etbo5ly Team.",
                color = MaterialTheme.colorScheme.onSurfaceVariant // Uses muted hint color
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Close",
                    color = MaterialTheme.colorScheme.primary // Turquoise or Deep Red
                )
            }
        }
    )
}
