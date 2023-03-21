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
import kids.baba.mobile.presentation.viewmodel.GrowthAlbumViewModel
import java.time.LocalDate
import java.time.YearMonth

//TODO api 연동
// 사용한 오픈소스 달력
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
    private var isChange = false
    private var isPick = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
        initializeCalendar()

        //앨범에 있는 아이템의 날짜를 이용해서 해당 앨범의 인덱스를 구해야한다.
        binding.tvDate.setOnClickListener {
            binding.viewPager.setCurrentItem(15, false)
        }
    }

    private fun initializeCalendar() {
        binding.myCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)

            override fun create(view: View): DayViewContainer = DayViewContainer(view, binding)

        }
        binding.myCalendar.weekScrollListener = { weekDays ->
            binding.tvDate.text = getWeekPageTitle(weekDays)
        }
        val currentMonth = YearMonth.now()
        binding.myCalendar.setup(
            currentMonth.minusMonths(5).atStartOfMonth(),
            currentMonth.plusMonths(5).atEndOfMonth(),
            firstDayOfWeekFromLocale(),
        )
        //
        binding.myCalendar.scrollPaged = false
        binding.myCalendar.scrollToWeek(LocalDate.now())
        binding.tvDate.setOnClickListener {
            binding.myCalendar.smoothScrollToDate(LocalDate.now())
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
            adapter.setItem(
                Album(
                    1,
                    "손제인",
                    "엄마",
                    "2023-01-it",
                    "빵긋빵긋",
                    false,
                    "www.naver.com",
                    "CARD_STYLE_1"
                )
            )
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
                //뷰페이저의 앨범에있는 날짜를 기준으로 달력 선택하도록 한다.
            }
        })
        binding.viewPager.currentItem
    }

    private fun initView() {
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
            }, 2023, 3, 12) {
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