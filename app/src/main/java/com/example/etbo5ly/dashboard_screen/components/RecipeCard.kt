package com.example.etbo5ly.dashboard_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.etbo5ly.R
import com.example.etbo5ly.data.dto.Meal
import com.example.etbo5ly.ui.theme.Etbo5lyTheme

@Composable
fun RecipeCard(onFavClick : ()-> Unit, isFavorite : Boolean, modifier: Modifier , meal: Meal) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            //Background Image
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = meal.strMeal,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            //shade
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            //TOP RIGHT (Category)
            Text(
                text = meal.strCategory,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.tertiary),
            ){
                //BOTTOM CONTENT
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.tertiary
                        )
                ) {

                    Text(
                        text = meal.strMeal,
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        // Country
                        Text(
                            modifier = Modifier
                                .padding(),
                            text = meal.strArea,
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        // Favorite Icon Button
                        IconButton(
                            onClick = {
                                onFavClick()
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite
                                else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color.Red else Color.White
                            )
                        }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRecipeCard(){
    Etbo5lyTheme {
       // RecipeCard({},true)
    }
}