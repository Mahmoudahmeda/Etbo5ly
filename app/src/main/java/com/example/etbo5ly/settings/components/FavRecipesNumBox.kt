package com.example.etbo5ly.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.etbo5ly.ui.theme.AppBarColor
import com.example.etbo5ly.ui.theme.FrameGray
import com.example.etbo5ly.ui.theme.Subtitle

@Composable
fun FavRecipesNumBox(number: Int){
    Surface(
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, FrameGray),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(120.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(number.toString(), color = AppBarColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Recipes", color = Subtitle, fontSize = 12.sp)
        }
    }
}