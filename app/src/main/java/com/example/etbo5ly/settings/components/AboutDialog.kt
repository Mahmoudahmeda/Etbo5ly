package com.example.etbo5ly.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.etbo5ly.R

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
                text = stringResource(R.string.about_etbo5ly), 
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = stringResource(R.string.about_desc) + "\n\n" + stringResource(R.string.version_format, appVersion),
                color = MaterialTheme.colorScheme.onSurfaceVariant // Uses muted hint color
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.close),
                    color = MaterialTheme.colorScheme.primary // Turquoise or Deep Red
                )
            }
        }
    )
}
