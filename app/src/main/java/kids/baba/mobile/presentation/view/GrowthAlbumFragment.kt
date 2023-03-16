package kids.baba.mobile.presentation.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentGrowthalbumBinding
import kids.baba.mobile.databinding.ItemCalendarBinding
import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.presentation.adapter.BabyAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kids.baba.mobile.presentation.util.MyDatePickerDialog
import kids.baba.mobile.presentation.viewmodel.GrowthAlbumViewModel
import kotlinx.coroutines.flow.catch
import java.lang.Math.abs
import java.util.*

//TODO api 연동
// 사용한 오픈소스 달력
// https://github.com/miso01/SingleRowCalendar
@AndroidEntryPoint
class GrowthAlbumFragment : Fragment() {

    private var _binding: FragmentGrowthalbumBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    val viewModel: GrowthAlbumViewModel by viewModels()
    //TODO datepicker 대신 달력 커스터 마이징
    // 앨범이 있는날짜에 표시해야함
    lateinit var datePicker: DatePickerDialog
    private val adapter = AlbumAdapter()
    private val babyAdapter = BabyAdapter()
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var isChange = false
    private var isPick = false
    private lateinit var singleRowCalendar: SingleRowCalendar
    private val mySelectionManager = object : CalendarSelectionManager {
        override fun canBeItemSelected(position: Int, date: Date): Boolean {
            val cal = Calendar.getInstance()
            cal.time = date
            return when (cal[Calendar.DAY_OF_WEEK]) {
                else -> true
            }
        }
    }
    private val myCalendarChangesObserver = object :
        CalendarChangesObserver {
        override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
            binding.tvDate.text =
                "${DateUtils.getYear(date)}.${DateUtils.getMonthNumber(date)}"
            binding.tvDay.text = DateUtils.getDayName(date)
            //TODO 달력 날짜 터치시 해당 날짜의 뷰페이저 아이템으로 이동
            //터치했을때만 처리 binding.viewPager.setCurrentItem(position,true)
            super.whenSelectionChanged(isSelected, position, date)
        }
    }
    private val myCalendarViewManager = object :
        CalendarViewManager {
        override fun setCalendarViewResourceId(
            position: Int,
            date: Date,
            isSelected: Boolean
        ): Int {
            val cal = Calendar.getInstance()
            cal.time = date
            return if (isSelected)
                when (cal[Calendar.DAY_OF_WEEK]) {
                    Calendar.MONDAY -> R.layout.selected_calendar_item
                    Calendar.WEDNESDAY -> R.layout.selected_calendar_item
                    Calendar.FRIDAY -> R.layout.selected_calendar_item
                    else -> R.layout.selected_calendar_item
                }
            else
                when (cal[Calendar.DAY_OF_WEEK]) {
                    Calendar.MONDAY -> R.layout.item_calendar
                    Calendar.WEDNESDAY -> R.layout.item_calendar
                    Calendar.FRIDAY -> R.layout.item_calendar
                    else -> R.layout.item_calendar
                }
        }

        override fun bindDataToCalendarView(
            holder: SingleRowCalendarAdapter.CalendarViewHolder,
            date: Date,
            position: Int,
            isSelected: Boolean
        ) {
            val calendarItem = ItemCalendarBinding.bind(holder.itemView)
            calendarItem.tvDateCalendarItem.text = DateUtils.getDayNumber(date)
            calendarItem.tvDayCalendarItem.text = DateUtils.getDay3LettersName(date)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
        initCalendar()
    }

    private fun initCalendar() {
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        // enable white status bar with black icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            requireActivity().window.statusBarColor = Color.WHITE
        }


        singleRowCalendar = binding.myCalendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            //TODO 캘린더에 날짜 무한으로 넣기
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++
        if (currentMonth == 12) {
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth--
        if (currentMonth == -1) {
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }
    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun collectUiState() {
        repeatOnStarted {
            viewModel.growthAlbumState.collect { state ->
                when (state) {
                    is GrowthAlbumState.UnInitialized -> initialize()
                    is GrowthAlbumState.Loading -> loading()
                    is GrowthAlbumState.SuccessAlbum -> setAlbumData(state)
                    is GrowthAlbumState.SuccessBaby -> setBabyData(state)
                    is GrowthAlbumState.Error -> catchError(state)
                    else -> {}
                }
            }
        }
    }

    private fun catchError(state: GrowthAlbumState.Error) {
        Log.e("error", "${state.t.message}")
    }

    private fun setBabyData(state: GrowthAlbumState.SuccessBaby) {
        state.data.forEach {
            Log.e("baby", "$it")
        }
    }

    private fun setAlbumData(state: GrowthAlbumState.SuccessAlbum) {
        state.data.forEach {
            Log.e("album", "$it")
        }
    }


    private fun loading() {
        Log.e("loading", "loading")
    }

    private suspend fun initialize() {
        Log.e("state", "initialize")
        initializeAlbumHolder()
        binding.babyList.adapter = babyAdapter
        binding.babyList.layoutManager = LinearLayoutManager(requireContext())
        //TODO api 연동시 앨범에 데이터를 할당해주기 (뷰페이저 앨범 - 달력) 1대1 매칭
        repeat(31) {
            adapter.setItem(Album(it + 1, "", "", "", "", false, "", ""))
        }
        repeat(5) {
            //TODO 아이 선택시 해당 아이의 앨범으로 가도록 처리
            babyAdapter.setItem(Baby("$it", "$it", "$it"))
        }
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        binding.babySelectView.maxHeight = 0
        binding.shadow.alpha = 0f
        viewModel.loadAlbum(1)
        viewModel.loadBaby()
        viewModel.pickDate.observe(viewLifecycleOwner) {
            isPick = if (!isPick) {
                datePicker.show()
                binding.shadow.alpha = 0.3f
                true
            } else {
                binding.shadow.alpha = 0f
                false
            }
        }
        viewModel.motionLayoutTransition.observe(viewLifecycleOwner) {
            isChange = if (!isChange) {
                binding.babySelectView.maxHeight = width * 3 / 2
                binding.babySelectView.isGone = false
                binding.shadow.alpha = 0.3f
                true
            } else {
                binding.babySelectView.isGone = true
                binding.babySelectView.maxHeight = 0
                binding.shadow.alpha = 0f
                false
            }
        }

    }

    fun onBackPressed(): Boolean {
        if (isChange) viewModel.motionLayoutTransition.value = Unit
        if (isPick) viewModel.pickDate.value = Unit
        return true
    }

    private fun initializeAlbumHolder() {
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
        }

        binding.viewPager.setPageTransformer(pageTransformer)

        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.viewPager.addItemDecoration(itemDecoration)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //TODO 뷰페이저 넘길때 날짜가 중앙으로 가도록 수정
                singleRowCalendar
                singleRowCalendar.select(position)
                singleRowCalendar.scrollToPosition(position)
            }
        })
        binding.viewPager.currentItem
    }

    private fun initView() {
        binding.viewmodel = viewModel
        datePicker =
            MyDatePickerDialog(requireContext(), listener = { view, year, month, dayOfMonth ->
                val year1 = datePicker.datePicker.year
                val month1 = datePicker.datePicker.month
                val day1 = datePicker.datePicker.dayOfMonth
                //TODO 날짜 선택시 해당 날짜의 뷰페이저로 이동
                binding.shadow.alpha = 0f
                Toast.makeText(requireContext(), "$year1 $month1, $day1", Toast.LENGTH_SHORT)
                    .show()
            }, 2023, 3, 12){
                binding.shadow.alpha = 0f
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrowthalbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}