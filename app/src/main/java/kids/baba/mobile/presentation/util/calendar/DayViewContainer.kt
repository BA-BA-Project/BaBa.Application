package com.example.calendarnew

import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.view.ViewContainer
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentGrowthalbumBinding
import kids.baba.mobile.databinding.ItemDayBinding
import kids.baba.mobile.presentation.util.calendar.DayListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DayViewContainer(view: View, binding: FragmentGrowthalbumBinding) : ViewContainer(view) {
    val bind = ItemDayBinding.bind(view)
    lateinit var day: WeekDay
    private var selectedDate = LocalDate.now()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private var listener: DayListener? = null

    fun setOnSelectedDateChangeListener(listener: DayListener) {
        this.listener = listener
    }

    init {
        view.setOnClickListener {
            if (selectedDate != day.date) {
                val oldDate = selectedDate
                selectedDate = day.date
                listener?.selectDay(selectedDate)
                binding.myCalendar.notifyDateChanged(day.date)
                oldDate?.let { binding.myCalendar.notifyDateChanged(it) }
            }
        }
    }

    fun bind(day: WeekDay) {
        this.day = day
        bind.exSevenDateText.text = dateFormatter.format(day.date)
        bind.exSevenDayText.text = day.date.dayOfWeek.displayText()

        val colorRes = if (day.date == selectedDate) {
            R.color.teal_200
        } else {
            R.color.teal_700
        }
        bind.exSevenDateText.setTextColor(view.context.getColorCompat(colorRes))
        bind.exSevenSelectedView.isVisible = day.date == selectedDate
    }
}
