package com.example.etbo5ly.dashboard_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.etbo5ly.settings.SettingsViewModel
import com.example.etbo5ly.ui.theme.CardBottom

@Composable
fun DrawerContent(
    userName: String,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val settingsViewModel: SettingsViewModel = viewModel()
    val userPhotoUrl by settingsViewModel.userPhotoUrl.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // User Avatar + Name
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = userPhotoUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(CardBottom),
                contentScale = ContentScale.Crop,
                error = rememberVectorPainter(Icons.Default.Person),
                fallback = rememberVectorPainter(Icons.Default.Person)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = userName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Item
        DrawerItem(
            icon = Icons.Default.Person,
            label = "Profile",
            tint = MaterialTheme.colorScheme.onSurface,
            onClick = onProfileClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Logout Item
        DrawerItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            label = "Logout",
            tint = Color.Red,
            onClick = onLogoutClick
        )
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                color = tint,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}