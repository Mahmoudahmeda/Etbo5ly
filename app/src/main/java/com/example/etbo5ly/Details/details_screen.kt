package com.example.etbo5ly

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecipeDetailsScreen(
    onNavigateBack: () -> Unit
) {

    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF13171F))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // 1. TOP APP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Recipe Details",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* TODO: Search */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
        }

        // 2. HERO IMAGE & TAGS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.chef),
                contentDescription = "Food Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RecipeTag("Main Course")
                RecipeTag("ma7shi-friendly")
            }
        }

        // 3. TITLE & QUICK INFO
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Very good ma7hshi",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoItem(icon = Icons.Default.Star, text = "4.8 (124)", iconTint = Color.Yellow)
                InfoItem(icon = Icons.Default.CheckCircle, text = "25 min")
                InfoItem(icon = Icons.Default.Build, text = "Medium")
            }

            Spacer(modifier = Modifier.height(24.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00EDFF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Start Cooking", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }


                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFF232832), RoundedCornerShape(16.dp))
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(16.dp))
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. STATS CARD
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF232832), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatColumn(icon = Icons.Default.Person, title = "SERVINGS", value = "2 People")
                Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.DarkGray))
                StatColumn(icon = Icons.Default.Add, title = "CALORIES", value = "450 Kcal")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 6. INGREDIENTS LIST
            SectionHeader(title = "Ingredients", subtitle = "4 items")
            Spacer(modifier = Modifier.height(12.dp))
            repeat(4) {
                IngredientItem(name = "Fresh Salmon", amount = "2 fillets")
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 7. INSTRUCTIONS LIST
            Text("Instructions", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            InstructionItem(
                stepNumber = "1",
                text = "Season the ma7shi with more ma7shi and then add even more ma7shi"
            )
            Spacer(modifier = Modifier.height(16.dp))
            InstructionItem(
                stepNumber = "2",
                text = "And after the first step..... add few more kilos of ma7shi then you drink a cup of tea for the diet"
            )
        }
    }
}

@Composable
fun RecipeTag(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFF00EDFF).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String, iconTint: Color = Color.Gray) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun StatColumn(icon: ImageVector, title: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun IngredientItem(name: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF232832), RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        Text(amount, color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun InstructionItem(stepNumber: String, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(Color(0xFF003D42), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(stepNumber, color = Color(0xFF00EDFF), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, color = Color.White, fontSize = 15.sp, lineHeight = 22.sp)
    }
}

@Preview(showSystemUi = true)
@Composable
fun RecipeDetailsScreenPreview() {
    RecipeDetailsScreen(onNavigateBack = {})
}