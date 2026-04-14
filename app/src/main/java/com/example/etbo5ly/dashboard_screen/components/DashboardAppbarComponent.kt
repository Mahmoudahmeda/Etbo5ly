package com.example.etbo5ly.dashboard_screen.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.etbo5ly.ui.theme.AppBarColor
import com.example.etbo5ly.ui.theme.AppBarColorShade
import com.example.etbo5ly.ui.theme.Etbo5lyTheme

@Composable
fun DashboardAppBarComponent(
    name: String,
    modifier: Modifier = Modifier
){
    val gradient = Brush.verticalGradient(
        colors = listOf(AppBarColor, AppBarColorShade)
    )


    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(gradient)
            .padding(horizontal = 20.dp, vertical = 34.dp) // better spacing
    ) {
        Column {
            Spacer(modifier = Modifier.height(6.dp))
            Row{
                Text(
                    text = "Hello $name 👋",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Ready to cook something delicious today?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary
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