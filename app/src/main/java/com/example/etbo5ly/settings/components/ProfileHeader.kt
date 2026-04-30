package com.example.etbo5ly.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.etbo5ly.ui.theme.*

@Composable
fun ProfileHeader(name: String, email: String, photoUrl: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = photoUrl ?: Icons.Default.Person,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(CardBottom),
            contentScale = ContentScale.Crop,
            error = rememberVectorPainter(Icons.Default.Person),
            fallback = rememberVectorPainter(Icons.Default.Person)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(name, color = ProductTitle, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(email, color = Subtitle, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
    }
}
