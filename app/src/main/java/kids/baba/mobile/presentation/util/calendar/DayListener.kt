package kids.baba.mobile.presentation.util.calendar

import java.time.LocalDate

interface DayListener {
    fun selectDay(date: LocalDate)

    fun releaseDay(date: LocalDate)
}