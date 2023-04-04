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
import kids.baba.mobile.presentation.adapter.BabyAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kids.baba.mobile.presentation.util.MyDatePickerDialog
import kids.baba.mobile.presentation.util.calendar.DayListener
import kids.baba.mobile.presentation.viewmodel.AlbumDetailViewModel
import kids.baba.mobile.presentation.viewmodel.GrowthAlbumViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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

    private val viewModel: GrowthAlbumViewModel by viewModels()
    private val detailViewModel: AlbumDetailViewModel by viewModels()

    private val dateToString = hashMapOf<LocalDate, String>()
    private val stringToInt = hashMapOf<String, Int>()
    private val intToDate = hashMapOf<Int, LocalDate>()

    private val albumAdapter = AlbumAdapter()
    private val myBabyAdapter = BabyAdapter()
    private val otherBabyAdapter = BabyAdapter()


    private lateinit var datePicker: DatePickerDialog
    private lateinit var dayViewContainer: DayViewContainer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collect()
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

    private fun collect() {
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
        repeatOnStarted {
            viewModel.myBabies.collect {
                it?.forEach { baby -> myBabyAdapter.setItem(baby) {} }
            }
        }
        repeatOnStarted {
            viewModel.othersBabies.collect {
                it?.forEach { baby -> otherBabyAdapter.setItem(baby) {} }
            }
        }
        repeatOnStarted {
            viewModel.albums.collect {
                it.forEachIndexed { index, album ->
                    albumAdapter.setItem(album)
                    mappingDateToIndex(album, index)
                }
            }
        }
    }

    private fun initialize() {
        initializeBinding()
        initializeCalendar()
        initializeDatePicker()
        initializeAlbumHolder()
        initializeBabyAdapter()
        initializeWidth()
    }

    private fun loading() {
        Log.e("loading", "loading")
    }

    private fun setAlbumData(state: GrowthAlbumState.SuccessAlbum) {
        Log.e("album", "${state.data}")
        viewModel.albums.value = state.data
    }

    private fun setBabyData(state: GrowthAlbumState.SuccessBaby) {
        val now = LocalDate.now()
        viewModel.myBabies.value = state.data.myBaby
        viewModel.othersBabies.value = state.data.others
        state.data.myBaby.let {
            //일단 첫번째 아이 기준으로 합니다.
            detailViewModel.baby.value = it[0]
            viewModel.baby.value = it[0]
            viewModel.loadAlbum(it[0].babyId, now.year, now.month.value)
        }
    }


    private fun pickDate() {
        datePicker.show()
        binding.shadow.alpha = 0.3f
        viewModel.growthAlbumState.value = GrowthAlbumState.Loading
    }

    private fun changeBaby() {
        binding.babySelectView.maxHeight = viewModel.width.value * 3 / 2
        binding.babySelectView.isGone = false
        binding.shadow.alpha = 0.3f
        viewModel.growthAlbumState.value = GrowthAlbumState.Loading
    }

    private fun catchError(state: GrowthAlbumState.Error) {
        Log.e("error", "${state.t.message}")
    }

    private fun initializeCalendar() {
        binding.myCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)

            override fun create(view: View): DayViewContainer {
                dayViewContainer = DayViewContainer(view, binding)
                dayViewContainer.setOnSelectedDateChangeListener(object : DayListener {
                    override fun selectDay(date: LocalDate) {
                        dateToString[date]?.let {
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
            viewModel.date.value = getWeekPageTitle(weekDays)
        }
        val currentMonth = YearMonth.now()
        binding.myCalendar.setup(
            currentMonth.minusMonths(600).atStartOfMonth(),
            currentMonth.plusMonths(600).atEndOfMonth(),
            firstDayOfWeekFromLocale(),
        )
        binding.myCalendar.scrollPaged = false
        binding.myCalendar.scrollToWeek(LocalDate.now())
        binding.tvAlbumTitle.setOnClickListener {
            detailViewModel.album.value =
                viewModel.albums.value[binding.viewPager.currentItem].toAlbumUiModel()
            val albumDetailDialog = AlbumDetailDialog(detailViewModel)
            albumDetailDialog.show(parentFragmentManager, "AlbumDetail")
        }

    }

    private fun initializeBinding() {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
    }

    private fun initializeWidth() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        viewModel.width.value = displayMetrics.widthPixels
    }

    private fun initializeBabyAdapter() {
        binding.myBabyList.adapter = myBabyAdapter
        binding.myBabyList.layoutManager = LinearLayoutManager(requireContext())
        binding.othersBabyList.adapter = otherBabyAdapter
        binding.othersBabyList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initializeDatePicker() {
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
    }

    private fun initializeAlbumHolder() {
        binding.viewPager.adapter = albumAdapter
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
                        binding.myCalendar.scrollBy(-viewModel.width.value / 2 + 72, 0)
                    }
                }
            }
        })
    }

    private fun mappingDateToIndex(album: Album, index: Int) {
        val localDate = parseLocalDate(album.date)
        dateToString[localDate] = album.date
        stringToInt[album.date] = index
        intToDate[index] = localDate
    }
    private fun parseLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
    }


    fun onKeyDown(): Boolean {
        binding.babySelectView.isGone = true
        binding.babySelectView.maxHeight = 0
        binding.shadow.alpha = 0f
        return true
    }
}