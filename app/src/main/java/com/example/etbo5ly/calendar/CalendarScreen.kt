package com.example.etbo5ly.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.etbo5ly.data.local.Etbo5lyDataBase
import com.example.etbo5ly.data.local.entities.CalendarEntity
import com.example.etbo5ly.data.repository.CalendarRepository
import com.example.etbo5ly.data.repository.MealRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
){
    val context = LocalContext.current

    // 1. Get your repository (you might have a singleton for this elsewhere)
    val database = remember { Etbo5lyDataBase.getDataBase(context) }
    val repository = remember { CalendarRepository(database.mealDao()) }

    // 2. Pass the factory to the viewModel() function
    val viewModel: CalendarViewModel = viewModel(
        factory = CalendarViewModelFactory(repository)
    )
    val uiState by viewModel.uiState.collectAsState()
    val scrollstate = rememberLazyListState()

    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Meal Calendar", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            HorizontalDateStrip(
                selectedDate = uiState.selectedDate,
                onDateSelected = { viewModel.onDateSelected(it) },
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                state = scrollstate,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {
                item {
                    DayHeader(
                        dayName = uiState.selectedDayName,
                        dateString = uiState.selectedDateFormatted,
                        timestamp = uiState.selectedDate,
                        viewModel
                    )
                }

                if (uiState.mealsForSelectedDay.isEmpty()) {
                    item { EmptyCalendarPlaceholder() }
                } else {
                    items(uiState.mealsForSelectedDay) { meal ->
                        MealCard(
                            meal = meal,
                            onDelete = { viewModel.deleteMeal(meal) },
                            viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DayHeader(
    dayName: String,
    dateString: String,
    timestamp: Long,
    viewModel: CalendarViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Show the "Today" badge if the timestamp matches the system date
            if (viewModel.isToday(timestamp)) {
                Spacer(modifier = Modifier.width(12.dp))
                TodayBadge()
            }
        }

        Text(
            text = dateString,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun TodayBadge() {
    Surface(
        color = Color(0xFF004D40), // Dark Teal background
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Today",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
            color = Color(0xFF4DB6AC), // Bright Teal text
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun HorizontalDateStrip(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    viewModel: CalendarViewModel
) {
    val weekDays = remember { viewModel.getCurrentWeekDays() }
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(weekDays) { day ->
            DateItem(
                dayName = day.shortName,
                dayNumber = day.number,
                isSelected = viewModel.isSameDay(day.timestamp, selectedDate),
                onClick = { onDateSelected(day.timestamp) }
            )
        }
    }
}

@Composable
fun DateItem(dayName: String, dayNumber: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFF4DB6AC) else Color.Transparent)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Text(dayName, color = if (isSelected) Color.Black else Color.Gray)
        Text(dayNumber, color = if (isSelected) Color.Black else Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EmptyCalendarPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No meals planned for this day",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
@Composable
fun MealCard(
    meal: CalendarEntity,
    onDelete: () -> Unit,
    viewModel: CalendarViewModel
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFF1E1E1E) // Darker gray for the card
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Meal Image
            AsyncImage(
                model = meal.mealImage,
                contentDescription = meal.mealName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Meal Info (Name and Category)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = meal.mealName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Planned for ${viewModel.formatTime(meal.date)}", // Helper to show time
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // 3. Delete Button
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFEF5350) // Soft Red
                )
            }
        }
    }
}