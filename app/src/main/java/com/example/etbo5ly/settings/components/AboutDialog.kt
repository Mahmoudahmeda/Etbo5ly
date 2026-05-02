package com.example.etbo5ly.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.etbo5ly.ui.theme.ProductTitle
import com.example.etbo5ly.ui.theme.Subtitle

@Composable
fun AboutDialog(
    onDismiss: () -> Unit,
    appVersion: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "About Etbo5ly", color = ProductTitle)
        },
        text = {
            Text(
                text = "Etbo5ly is your personal meal planning companion.\n\n" +
                        "Version: $appVersion\n" +
                        "Developed by the Etbo5ly Team.",
                color = Subtitle
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
