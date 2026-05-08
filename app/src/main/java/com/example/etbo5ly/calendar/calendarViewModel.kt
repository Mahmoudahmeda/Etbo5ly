package com.example.etbo5ly.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.local.entities.CalendarEntity
import com.example.etbo5ly.data.repository.CalendarRepository
import com.example.etbo5ly.data.repository.MealRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarViewModel(
    private val repository: CalendarRepository,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val currentUserId: String
        get() = auth.currentUser?.uid ?: ""

    init {
        loadMealsForCurrentWeek()
    }

    private fun loadMealsForCurrentWeek() {
        val (start, end) = getWeekRange(System.currentTimeMillis())
        viewModelScope.launch {
            repository.getMealsForWeek(currentUserId, start, end).collect { meals ->
                _uiState.update { state ->
                    state.copy(
                        mealsForWeek = meals,
                        mealsForSelectedDay = meals.filter { isSameDay(it.date, state.selectedDate) }
                    )
                }
            }
        }
    }

    fun onDateSelected(timestamp: Long) {
        val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(timestamp))
        val fullDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))

        _uiState.update { state ->
            state.copy(
                selectedDate = timestamp,
                selectedDayName = dayName,
                selectedDateFormatted = fullDate,
                // Re-filter when the date changes
                mealsForSelectedDay = state.mealsForWeek.filter { isSameDay(it.date, timestamp) }
            )
        }
    }

    fun deleteMeal(meal: CalendarEntity) {
        viewModelScope.launch {
            repository.deleteMealFromCalendar(meal)
        }
    }

    fun isToday(timestamp: Long): Boolean = isSameDay(timestamp, System.currentTimeMillis())

    fun isSameDay(t1: Long, t2: Long): Boolean {
        val fmt = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return fmt.format(Date(t1)) == fmt.format(Date(t2))
    }

    private fun getWeekRange(timestamp: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val start = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        return Pair(start, calendar.timeInMillis)
    }
    fun getCurrentWeekDays(): List<DateModel> {
        val days = mutableListOf<DateModel>()
        val cal = Calendar.getInstance()
        // Start at the beginning of the current week (Sunday)
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val dayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dayNumberFormat = SimpleDateFormat("dd", Locale.getDefault())

        for (i in 0..6) {
            days.add(
                DateModel(
                    shortName = dayNameFormat.format(cal.time),
                    number = dayNumberFormat.format(cal.time),
                    timestamp = cal.timeInMillis
                )
            )
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return days
    }
    fun formatTime(timestamp: Long): String {
        // "hh:mm a" will show "01:30 PM" or "09:00 AM"
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

class CalendarViewModelFactory(
    private val repository: CalendarRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}