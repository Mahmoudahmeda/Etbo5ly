package com.example.etbo5ly.settings.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.etbo5ly.ui.theme.CardBottom

@Composable
fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBottom),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        content = content
    )
}
