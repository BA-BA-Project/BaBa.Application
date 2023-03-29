//package com.example.calendarnew
//
//import android.util.Log
//import android.view.View
//import com.kizitonwose.calendar.core.WeekDay
//import com.kizitonwose.calendar.view.ViewContainer
//import kids.baba.mobile.R
//import kids.baba.mobile.databinding.FragmentGrowthalbumBinding
//import kids.baba.mobile.databinding.ItemDayBinding
//import kids.baba.mobile.presentation.util.calendar.DayListener
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//
//class DayViewContainer(view: View, private var selectedDate: LocalDate) : ViewContainer(view) {
//    val bind = ItemDayBinding.bind(view)
//    lateinit var day: WeekDay
//
//    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
//    private var listener: DayListener? = null
//
//    fun setOnSelectedDateChangeListener(listener: DayListener) {
//        this.listener = listener
//    }
//
//    init {
//        view.setOnClickListener {
//            if(selectedDate != day.date){
//                val oldDate = selectedDate
//                selectedDate = day.date
//                listener?.selectDay(oldDate, selectedDate)
//            }
//        }
//    }
//
//    fun bind(day: WeekDay) {
//        this.day = day
//        bind.tvDate.text = dateFormatter.format(day.date)
//        bind.tvWeekday.text = day.date.dayOfWeek.displayText()
//        bind.selected = selectedDate == day.date
//    }
//}
