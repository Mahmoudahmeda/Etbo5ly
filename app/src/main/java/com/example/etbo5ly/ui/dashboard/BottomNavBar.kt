package com.example.etbo5ly.ui.dashboard

import android.widget.Toast
import androidx.annotation.StringRes
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.etbo5ly.R

data class BottomNavItem(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(
    selectedItem: String,
    onItemClick: (String) -> Unit,
    navController: NavController
) {
    val navItems = remember {
        listOf(
            BottomNavItem("home", R.string.nav_home, Icons.Default.Home),
            BottomNavItem("Search", R.string.nav_search, Icons.Default.Search),
            BottomNavItem("Calendar", R.string.nav_calendar, Icons.Default.DateRange),
            BottomNavItem("Profile", R.string.nav_profile, Icons.Default.Person)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            navItems.forEach { item ->
                val isSelected = item.route == selectedItem
                val label = stringResource(item.labelRes)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            onItemClick(item.route)
                            navController.navigate(item.route)
                        }
                        .padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = label,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        // Match text color with icon color
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}