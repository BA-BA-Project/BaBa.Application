package kids.baba.mobile.presentation.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.calendarnew.DayViewContainer
import com.example.calendarnew.getWeekPageTitle
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentGrowthalbumBinding
import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.presentation.adapter.BabyAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kids.baba.mobile.presentation.util.MyDatePickerDialog
import kids.baba.mobile.presentation.util.calendar.DayListener
import kids.baba.mobile.presentation.viewmodel.GrowthAlbumViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

//TODO 앨범 있는 날짜 횡달력에 표시
//TODO 횡달력에서 선택한 날짜 표시
//TODO 횡달력에서 선택한 날짜 중앙정렬 자연스럽게 하기(되긴하는데 삐걱임)
//TODO datePicker 대신 달력 커스터 마이징
//TODO api 연동(dummySource대신 불러온 데이터 넣기만 하면됨)
//TODO viewPager - yyyy-mm dateview 연동 자연스럽게 하기(약간 싱크가 안맞음)

// 사용한 오픈소스 달력
@AndroidEntryPoint
class GrowthAlbumFragment : Fragment() {

    private var _binding: FragmentGrowthalbumBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    val viewModel: GrowthAlbumViewModel by viewModels()
    val dateToString = hashMapOf<LocalDate, String>()
    val stringToInt = hashMapOf<String, Int>()
    val intToDate = hashMapOf<Int, LocalDate>()
    private var width: Int = 0
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    lateinit var datePicker: DatePickerDialog
    private val adapter = AlbumAdapter()
    private val babyAdapter = BabyAdapter()
    private lateinit var dayViewContainer: DayViewContainer
    private var currentDay = LocalDate.now()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUiState()
        initializeCalendar()
    }

    private fun initializeCalendar() {
        binding.myCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)

            override fun create(view: View): DayViewContainer {
                dayViewContainer = DayViewContainer(view, binding)
                dayViewContainer.setOnSelectedDateChangeListener(object : DayListener {
                    override fun selectDay(date: LocalDate) {
                        currentDay = date
                        dateToString[currentDay]?.let {
                            binding.viewPager.setCurrentItem(stringToInt[it]!!, true)
                        }
                    }

                    override fun releaseDay(date: LocalDate) {

                    }
                })
                return dayViewContainer
            }

        }
        binding.myCalendar.weekScrollListener = { weekDays ->
            binding.tvDate.text = getWeekPageTitle(weekDays)
        }
        val currentMonth = YearMonth.now()
        binding.myCalendar.setup(
            currentMonth.minusMonths(600).atStartOfMonth(),
            currentMonth.plusMonths(600).atEndOfMonth(),
            firstDayOfWeekFromLocale(),
        )
        //
        binding.myCalendar.scrollPaged = false
        binding.myCalendar.scrollToWeek(LocalDate.now())
        binding.tvAlbumTitle.setOnClickListener {
            val albumDetailDialog = AlbumDetailDialog()
            albumDetailDialog.show(parentFragmentManager, "AlbumDetail")
        }

    }

    private fun collectUiState() {
        repeatOnStarted {
            viewModel.growthAlbumState.collect { state ->
                when (state) {
                    is GrowthAlbumState.UnInitialized -> initialize()
                    is GrowthAlbumState.Loading -> loading()
                    is GrowthAlbumState.SuccessAlbum -> setAlbumData(state)
                    is GrowthAlbumState.SuccessBaby -> setBabyData(state)
                    is GrowthAlbumState.PickDate -> pickDate()
                    is GrowthAlbumState.ChangeBaby -> changeBaby()
                    is GrowthAlbumState.Error -> catchError(state)
                    else -> {}
                }
            }
        }
    }

    fun changeBaby() {
        Log.e("changeBaby","")
        binding.babySelectView.maxHeight = width * 3 / 2
        binding.babySelectView.isGone = false
        binding.shadow.alpha = 0.3f
        viewModel.growthAlbumState.value = GrowthAlbumState.Loading
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

    fun getDummyData(): List<Album> {
        val dummyResponse = mutableListOf<Album>()
        repeat(365) {
            currentDay = currentDay.plusDays(1)
            val album = Album(
                1,
                "Empty",
                "엄마",
                currentDay.toString(),
                "빵긋빵긋",
                false,
                "www.naver.com",
                "CARD_STYLE_1"
            )
            dummyResponse.add(album)
        }
        repeat(50) {
            val album = Album(
                1,
                "할당",
                "엄마",
                generateRandomDate(),
                "빵긋빵긋",
                false,
                "www.naver.com",
                "CARD_STYLE_1"
            )
            dummyResponse.add(album)
        }
        return dummyResponse
            .groupBy { it.date }
            .mapValues { (_, albums) ->
                when (albums.size) {
                    1 -> albums[0]
                    else -> albums.find { it.name.contains("할당") }
                        ?: albums[0]
                }
            }
            .values
            .toList()
            .sortedBy { LocalDate.parse(it.date) }
    }

    fun pickDate() {
        datePicker.show()
        binding.shadow.alpha = 0.3f
        viewModel.growthAlbumState.value = GrowthAlbumState.Loading
    }

    private fun initialize() {
        binding.viewmodel = viewModel
        datePicker =
            MyDatePickerDialog(requireContext(), listener = { _, _, _, _ ->
                val year = datePicker.datePicker.year
                val month = datePicker.datePicker.month
                val day = datePicker.datePicker.dayOfMonth
                binding.shadow.alpha = 0f
                binding.myCalendar.smoothScrollToWeek(
                    WeekDay(
                        LocalDate.of(year, month + 1, day),
                        position = WeekDayPosition.InDate
                    )
                )
                dateToString[LocalDate.of(year, month + 1, day)]?.let {
                    binding.viewPager.currentItem = stringToInt[it]!!
                }
            }, 2023, 3, 12) {
                binding.shadow.alpha = 0f
            }
        initializeAlbumHolder()
        binding.babyList.adapter = babyAdapter
        binding.babyList.layoutManager = LinearLayoutManager(requireContext())
        getDummyData().forEachIndexed { index, album ->
            adapter.setItem(album)
            val localDate = parseLocalDate(album.date)
            dateToString[localDate] = album.date
            stringToInt[album.date] = index
            intToDate[index] = localDate
        }
        currentDay = LocalDate.now()
        repeat(5) {
            babyAdapter.setItem(Baby("$it", "$it", "$it"))
        }
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels

        binding.babySelectView.maxHeight = 0
        binding.shadow.alpha = 0f
        viewModel.loadAlbum(1)
        viewModel.loadBaby()

    }

    fun generateRandomDate(): String {
        val currentDate = LocalDate.now()
        val randomDays = (0..100).random()
        val randomDate = currentDate.plusDays(randomDays.toLong())
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        return randomDate.format(formatter)
    }

    fun parseLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }

    fun onKeyDown(): Boolean {
        binding.babySelectView.isGone = true
        binding.babySelectView.maxHeight = 0
        binding.shadow.alpha = 0f
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
                intToDate[position]?.let {
                    lifecycleScope.launch {
                        binding.myCalendar.smoothScrollToDate(it)
                        delay(200)
                        binding.myCalendar.scrollBy(-width / 2 + 72, 0)
                    }
                }
            }
        })
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