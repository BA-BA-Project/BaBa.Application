package com.example.calendarnew

import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

fun YearMonth.displayText(): String {
    return "${this.year}.${this.month.value}"
}


fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    return firstDate.yearMonth.displayText()
}

