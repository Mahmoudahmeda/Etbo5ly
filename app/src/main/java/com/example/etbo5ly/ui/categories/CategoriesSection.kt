package com.example.etbo5ly.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.navigation.NavController

@Composable
fun CategoriesSection(
    categories: List<Category>,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Categories",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = "View All",
                fontSize = 13.sp,
                color = Color(0xFF51FBFB),
                modifier = Modifier.clickable {
                    navController.navigate("Search")
                }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        LazyRow {
            items(categories) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            navController.navigate("searchResult/category/${category.name}")
                        }
                ) {
                    AsyncImage(
                        model = category.image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 50.dp, height = 48.dp)
                            .clip(RoundedCornerShape(100.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = category.name,
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
