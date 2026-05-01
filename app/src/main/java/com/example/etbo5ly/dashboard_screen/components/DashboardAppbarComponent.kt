package com.example.etbo5ly.dashboard_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.etbo5ly.settings.SettingsViewModel
import com.example.etbo5ly.ui.theme.AppBarColor
import com.example.etbo5ly.ui.theme.AppBarColorShade
import com.example.etbo5ly.ui.theme.CardBottom
import com.example.etbo5ly.ui.theme.Etbo5lyTheme

@Composable
fun DashboardAppBarComponent(
    name: String,
    modifier: Modifier = Modifier
){
    val gradient = Brush.verticalGradient(
        colors = listOf(AppBarColor, AppBarColorShade)
    )
    
    val settingsViewModel: SettingsViewModel = viewModel()
    val userPhotoUrl by settingsViewModel.userPhotoUrl.collectAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(gradient)
            .padding(horizontal = 20.dp, vertical = 34.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Hello $name 👋",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Ready to cook something delicious today?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            AsyncImage(
                model = userPhotoUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(CardBottom),
                contentScale = ContentScale.Crop,
                error = rememberVectorPainter(Icons.Default.Person),
                fallback = rememberVectorPainter(Icons.Default.Person),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDashboardAppBar(){
    Etbo5lyTheme {
        DashboardAppBarComponent(name = "Ahmed")
    }
}
