package kids.baba.mobile.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentGrowthalbumBinding
import kids.baba.mobile.presentation.adapter.AlbumAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.GrowthAlbumViewModel
import java.time.LocalDate
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

    //    val dateToString = hashMapOf<LocalDate, String>()
//    val stringToInt = hashMapOf<String, Int>()
//    val intToDate = hashMapOf<Int, LocalDate>()
//    private var width: Int = 0
//    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
//    lateinit var datePicker: DatePickerDialog
    private val albumAdapter = AlbumAdapter()

    //    private val babyAdapter = BabyAdapter()
    private var currentDay = LocalDate.now()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        initializeAlbumHolder()
        collectUiState()
        collectCurrentDate()
        setCalendar()
//        initializeCalendar()
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        binding.dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd")
    }

    private fun setCalendar(){
//        binding.wcvAlbumCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
//            override fun create(view: View): DayViewContainer {
//                val dayViewContainer = DayViewContainer(view)
//                dayViewContainer.setOnSelectedDateChangeListener(object : DayListener {
//                    override fun selectDay(date: LocalDate) {
//                        binding.wcvAlbumCalendar.notifyDateChanged(currentDay)
//                        binding.wcvAlbumCalendar.notifyDateChanged(date)
//                    }
//
//                    override fun releaseDay(date: LocalDate) {
//
//                    }
//                })
//                return dayViewContainer
//            }
//
//            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
//        }
    }


//    private fun initializeCalendar() {
//        class DayViewContainer(view: View) : ViewContainer(view) {
//            val bind = ItemDayBinding.bind(view)
//            lateinit var day: WeekDay
//
//            private val dateFormatter = DateTimeFormatter.ofPattern("dd")
//            private var listener: DayListener? = null
//
//            fun setOnSelectedDateChangeListener(listener: DayListener) {
//                this.listener = listener
//            }
//
//            init {
//                view.setOnClickListener {
//                    if (currentDay != day.date) {
//                        val oldDate = currentDay
//                        currentDay = day.date
//                        listener?.selectDay(oldDate)
//                    }
//                }
//            }
//
//            fun bind(day: WeekDay) {
//                this.day = day
//                bind.tvDate.text = dateFormatter.format(day.date)
//                bind.tvWeekday.text = day.date.dayOfWeek.displayText()
//                bind.selected = currentDay == day.date
//            }
//        }
//
//        binding.wcvAlbumCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
//            override fun create(view: View): DayViewContainer {
//                val dayViewContainer = DayViewContainer(view)
//                dayViewContainer.setOnSelectedDateChangeListener(object : DayListener {
//                    override fun selectDay(date: LocalDate) {
//                        dateToString[currentDay]?.let {
//                            binding.vpBabyPhoto.setCurrentItem(stringToInt[it]!!, true)
//                        }
//                        binding.wcvAlbumCalendar.notifyDateChanged(currentDay)
//                        binding.wcvAlbumCalendar.notifyDateChanged(date)
//                    }
//
//                    override fun releaseDay(date: LocalDate) {
//
//                    }
//                })
//                return dayViewContainer
//            }
//
//            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
//        }
//        binding.wcvAlbumCalendar.weekScrollListener = { weekDays ->
//            binding.tvDate.text = getWeekPageTitle(weekDays)
//        }
//        val currentMonth = YearMonth.now()
//        binding.wcvAlbumCalendar.setup(
//            currentMonth.minusMonths(600).atStartOfMonth(),
//            currentMonth.plusMonths(600).atEndOfMonth(),
//            firstDayOfWeekFromLocale(),
//        )
//        binding.wcvAlbumCalendar.scrollPaged = false
//        binding.wcvAlbumCalendar.scrollToWeek(LocalDate.now())
//        binding.tvAlbumTitle.setOnClickListener {
//            val albumDetailDialog = AlbumDetailDialog()
//            albumDetailDialog.show(parentFragmentManager, "AlbumDetail")
//        }
//    }

    private fun collectUiState() {
        repeatOnStarted {
            viewModel.growthAlbumState.collect { state ->
//                when (state) {
//                    is GrowthAlbumState.UnInitialized -> initialize()
//                    is GrowthAlbumState.Loading -> loading()
//                    is GrowthAlbumState.SuccessAlbum -> setAlbumData(state)
//                    is GrowthAlbumState.SuccessBaby -> setBabyData(state)
//                    is GrowthAlbumState.PickDate -> pickDate()
//                    is GrowthAlbumState.ChangeBaby -> changeBaby()
//                    is GrowthAlbumState.Error -> catchError(state)
//                }
            }
        }
    }

    private fun collectCurrentDate() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.currentDate.collect {
                this.currentDay = it
            }
        }
    }

//    fun changeBaby() {
//        Log.e("changeBaby", "")
//        binding.babySelectView.maxHeight = width * 3 / 2
//        binding.babySelectView.isGone = false
//        viewModel.growthAlbumState.value = GrowthAlbumState.Loading
//    }
//
//    private fun catchError(state: GrowthAlbumState.Error) {
//        Log.e("error", "${state.t.message}")
//    }
//
//    private fun setBabyData(state: GrowthAlbumState.SuccessBaby) {
//        state.data.forEach {
//            Log.e("baby", "$it")
//        }
//    }
//
//    private fun setAlbumData(state: GrowthAlbumState.SuccessAlbum) {
//        state.data.forEach {
//            Log.e("album", "$it")
//        }
//    }
//
//
//    private fun loading() {
//        Log.e("loading", "loading")
//    }

//    fun getDummyData(): List<Album> {
//        val dummyResponse = mutableListOf<Album>()
//        repeat(365) {
//            currentDay = currentDay.plusDays(1)
//            val album = Album(
//                1,
//                "Empty",
//                "엄마",
//                currentDay.toString(),
//                "빵긋빵긋",
//                false,
//                "www.naver.com",
//                "CARD_STYLE_1"
//            )
//            dummyResponse.add(album)
//        }
//        repeat(50) {
//            val album = Album(
//                1,
//                "할당",
//                "엄마",
//                generateRandomDate(),
//                "빵긋빵긋",
//                false,
//                "www.naver.com",
//                "CARD_STYLE_1"
//            )
//            dummyResponse.add(album)
//        }
//        return dummyResponse
//            .groupBy { it.date }
//            .mapValues { (_, albums) ->
//                when (albums.size) {
//                    1 -> albums[0]
//                    else -> albums.find { it.name.contains("할당") }
//                        ?: albums[0]
//                }
//            }
//            .values
//            .toList()
//            .sortedBy { LocalDate.parse(it.date) }
//    }

    //    fun pickDate() {
//        datePicker.show()
//        viewModel.growthAlbumState.value = GrowthAlbumState.Loading
//    }
//
//    private fun initialize() {
//        datePicker =
//            MyDatePickerDialog(requireContext(), listener = { _, _, _, _ ->
//                val year = datePicker.datePicker.year
//                val month = datePicker.datePicker.month
//                val day = datePicker.datePicker.dayOfMonth
//                binding.wcvAlbumCalendar.smoothScrollToWeek(
//                    WeekDay(
//                        LocalDate.of(year, month + 1, day),
//                        position = WeekDayPosition.InDate
//                    )
//                )
//                dateToString[LocalDate.of(year, month + 1, day)]?.let {
//                    binding.vpBabyPhoto.currentItem = stringToInt[it]!!
//                }
//            }, 2023, 3, 12) {
//
//            }
//        initializeAlbumHolder()
//        binding.babyList.adapter = babyAdapter
//        binding.babyList.layoutManager = LinearLayoutManager(requireContext())
//        getDummyData().forEachIndexed { index, album ->
//            adapter.setItem(album)
//            val localDate = parseLocalDate(album.date)
//            dateToString[localDate] = album.date
//            stringToInt[album.date] = index
//            intToDate[index] = localDate
//        }
//        currentDay = LocalDate.now()
//        repeat(5) {
//            babyAdapter.setItem(Baby("$it", "$it", "$it"))
//        }
//        val displayMetrics = DisplayMetrics()
//        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
//        width = displayMetrics.widthPixels
//
//        binding.babySelectView.maxHeight = 0
//        viewModel.loadAlbum(1)
//        viewModel.loadBaby()
//
//    }
//
//    fun generateRandomDate(): String {
//        val currentDate = LocalDate.now()
//        val randomDays = (0..100).random()
//        val randomDate = currentDate.plusDays(randomDays.toLong())
//        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
//        return randomDate.format(formatter)
//    }
//
//    fun parseLocalDate(dateString: String): LocalDate {
//        return LocalDate.parse(dateString, formatter)
//    }
//
    fun onKeyDown(): Boolean {
//        binding.babySelectView.isGone = true
//        binding.babySelectView.maxHeight = 0
        return true
    }

    //
    private fun initializeAlbumHolder() {
        binding.vpBabyPhoto.adapter = albumAdapter
        viewLifecycleOwner.repeatOnStarted {
            viewModel.growthAlbumList.collect {
                albumAdapter.submitList(it)
            }
        }
//        binding.vpBabyPhoto.offscreenPageLimit = 1
//
//        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
//        val currentItemHorizontalMarginPx =
//            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
//        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
//        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
//            page.translationX = -pageTranslationX * position
//            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
//        }
//
//        binding.vpBabyPhoto.setPageTransformer(pageTransformer)
//
//        val itemDecoration = HorizontalMarginItemDecoration(
//            requireContext(),
//            R.dimen.viewpager_current_item_horizontal_margin
//        )
//        binding.vpBabyPhoto.addItemDecoration(itemDecoration)
        binding.vpBabyPhoto.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
//                intToDate[position]?.let {
//                    lifecycleScope.launch {
//                        binding.wcvAlbumCalendar.apply {
//                            smoothScrollToDate(it)
//                            val oldDay = intToDate[binding.vpBabyPhoto.currentItem]
//                            oldDay?.let {
//                                notifyDateChanged(intToDate[position]!!)
//                                notifyDateChanged(it)
//                            }
//                            scrollBy(-width / 2 + 72, 0)
//                        }
//                    }
//                }
                viewModel.selectAlbum(position)
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