package com.example.etbo5ly.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavBar(
    selectedItem: String,
    onItemClick: (String) -> Unit
) {
    val context = LocalContext.current
    val items = listOf("Home", "Search", "Calendar", "Profile")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1E2228).copy(alpha = 0.8f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { item ->
                val isSelected = item == selectedItem

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            onItemClick(item)
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = when (item) {
                            "Home"     -> Icons.Default.Home
                            "Search"   -> Icons.Default.Search
                            "Calendar" -> Icons.Default.DateRange
                            else       -> Icons.Default.Person
                        },
                        contentDescription = item,
                        tint = if (isSelected) Color(0xFF51FBFB) else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item,
                        fontSize = 12.sp,
                        color = if (isSelected) Color(0xFF51FBFB) else Color.White
                    )
                }
            }
        }
    }
}