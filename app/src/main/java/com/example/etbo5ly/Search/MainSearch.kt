package com.example.etbo5ly.Search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Egg
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.etbo5ly.Search.components.ResponseMealCard
import com.example.etbo5ly.Search.components.SearchGrid
import com.example.etbo5ly.ui.components.Etbo5lyAppBar
import com.example.etbo5ly.ui.theme.AppBarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSearch(
    navController: NavController,
    viewModel: Search = Search()
) {
    val searchQ by viewModel.searchQ.collectAsState()
    val result by viewModel.result.collectAsState()
    val meals = result?.meals ?: emptyList()

    var selectedFilter by remember { mutableStateOf("Categories") }
    val filters = listOf(
        FilterOption("Categories", Icons.Default.Restaurant),
        FilterOption("Countries", Icons.Default.Public),
        FilterOption("Ingredients", Icons.Outlined.Egg)
    )

    Scaffold(
        topBar = {
            Etbo5lyAppBar(navController = navController, text = "Search")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 16.dp)
        ) {
            // 1. Custom Search Bar
            TextField(
                value = searchQ,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = { Text("Search for recipes or ingredients...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1C1C1E),
                    unfocusedContainerColor = Color(0xFF1C1C1E),
                    disabledContainerColor = Color(0xFF1C1C1E),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true
            )

            // 2. Filter Buttons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter.name
                    FilterButton(
                        option = filter,
                        isSelected = isSelected,
                        onClick = { 
                            selectedFilter = filter.name
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 3. The Grid
            SearchGrid(
                items = meals,
                modifier = Modifier.weight(1f)
            ) { meal ->
                ResponseMealCard(meal = meal, navcontroller = navController)
            }
        }
    }
}

data class FilterOption(val name: String, val icon: ImageVector)

@Composable
fun FilterButton(
    option: FilterOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) AppBarColor else Color(0xFF1C1C1E))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (isSelected) Color.Black else Color.Gray
            )
            Text(
                text = option.name,
                color = if (isSelected) Color.Black else Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp),
                maxLines = 1
            )
        }
    }
}
