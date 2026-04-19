package com.example.etbo5ly.Search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.etbo5ly.Search.components.ResponseMealCard
import com.example.etbo5ly.ui.theme.AppBarColor
import com.example.etbo5ly.ui.theme.AppBarColorShade

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navcontroller: NavController, search: Search = Search()){

    val searchQ by search.searchQ.collectAsStateWithLifecycle()
    val result by search.result.collectAsStateWithLifecycle()
    var openedFilter by remember { mutableStateOf(false) }
    val filterOptions = listOf("General","Categories", "Countries", "Ingredients")
    var selectedFilter by remember { mutableStateOf("General") }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(text = "Search",color = Color.Black) },
            navigationIcon = {
                IconButton(onClick = { navcontroller.navigateUp() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarColors(
                containerColor = AppBarColor,
                titleContentColor = Color.Black,
                scrolledContainerColor = AppBarColorShade,
                navigationIconContentColor = Color.Black,
                actionIconContentColor = Color.Black
            )
        )}
    ) {paddingValues ->
        LazyVerticalGrid(
            GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(paddingValues)
        ){
            item(span = { GridItemSpan(2) }){
                Column{
                    SearchBar(
                        query = searchQ,
                        onQueryChange = {search.onSearchQueryChange(it)},
                        onSearch = {search.search(selectedFilter)},
                        active = false,
                        onActiveChange = {},
                        placeholder = {Text("What are you Looking For ?")},
                        leadingIcon = {
                            Box {
                                IconButton(onClick = { openedFilter = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Filter"
                                    )
                                }
                                DropdownMenu(
                                    expanded = openedFilter,
                                    onDismissRequest = { openedFilter = false }
                                ) {
                                    filterOptions.forEach { option ->
                                        val isSelected = option == selectedFilter
                                        DropdownMenuItem(
                                            text = { Text(
                                                option,
                                                fontWeight = if(isSelected) FontWeight.ExtraBold else FontWeight.Normal
                                            )},
                                            trailingIcon = {
                                                if (isSelected) Icon(Icons.Default.Check, contentDescription = null)
                                            },
                                            onClick = {
                                                selectedFilter = option
                                                openedFilter = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) {}
                }
            }
            val meals = result?.meals ?: emptyList()
            items(meals) { meal ->
                ResponseMealCard(meal, navcontroller)
            }
        }

    }
}