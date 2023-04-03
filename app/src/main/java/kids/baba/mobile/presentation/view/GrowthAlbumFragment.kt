package kids.baba.mobile.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.calendarnew.getWeekPageTitle
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentGrowthalbumBinding
import kids.baba.mobile.databinding.ItemDayBinding
import kids.baba.mobile.presentation.adapter.AlbumAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.BabyListBottomSheet.Companion.SELECTED_BABY_KEY
import kids.baba.mobile.presentation.viewmodel.GrowthAlbumViewModel
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


    //    val dateToString = hashMapOf<LocalDate, String>()
//    val stringToInt = hashMapOf<String, Int>()
//    val intToDate = hashMapOf<Int, LocalDate>()
//    private var width: Int = 0
//    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
//    lateinit var datePicker: DatePickerDialog
    private lateinit var albumAdapter: AlbumAdapter

    //    private val babyAdapter = BabyAdapter()
    private var selectedDate = LocalDate.now()
    private var isDateInit = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        collectUiState()
        collectSelectedDate()
        collectSelectedAlbum()
        initializeAlbumHolder()
        setCalendar()
        setBottomSheet()
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        binding.dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd")
    }

    private fun setBottomSheet() {
        binding.civBabyProfile.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(SELECTED_BABY_KEY, viewModel.selectedBaby.value)
            val bottomSheet = BabyListBottomSheet { baby ->
                viewModel.selectBaby(baby)
            }
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, BabyListBottomSheet.TAG)
        }
    }

    private fun setCalendar() {
        class DayViewContainer(view: View) : ViewContainer(view) {
            val bind = ItemDayBinding.bind(view)
            lateinit var day: WeekDay
            val formatter = DateTimeFormatter.ofPattern("dd")

            init {
                view.setOnClickListener {
                    if (selectedDate != day.date) {
                        viewModel.selectDate(day.date)
                    }
                }
            }

            fun bind(day: WeekDay) {
                this.day = day
                bind.date = day.date
                bind.selected = selectedDate == day.date
                bind.formatter = formatter


                view.isClickable = day.date.isAfter(LocalDate.now()).not()
                if (view.isClickable) {
                    val textColor0 = requireContext().getColor(R.color.text_0)
                    bind.tvDate.setTextColor(textColor0)
                    bind.tvWeekday.setTextColor(textColor0)
                } else {
                    val textColor3 = requireContext().getColor(R.color.text_3)
                    bind.tvDate.setTextColor(textColor3)
                    bind.tvWeekday.setTextColor(textColor3)
                }
            }
        }

        binding.wcvAlbumCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
        }

        binding.wcvAlbumCalendar.weekScrollListener = { weekDays ->
            binding.tvDate.text = getWeekPageTitle(weekDays)
        }

        val currentMonth = YearMonth.now()
        binding.wcvAlbumCalendar.setup(
            currentMonth.minusMonths(24).atStartOfMonth(),
            currentMonth.plusMonths(0).atEndOfMonth(),
            firstDayOfWeekFromLocale()
        )
        binding.wcvAlbumCalendar.scrollPaged = false
        binding.wcvAlbumCalendar.scrollToDate(LocalDate.now().minusDays(3))
    }


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

    private fun collectSelectedDate() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.selectedDate.collect {
                viewModel.selectAlbum()
                setSelectedDate(it)
            }
        }
    }


    fun onKeyDown(): Boolean {
//        binding.babySelectView.isGone = true
//        binding.babySelectView.maxHeight = 0
        return true
    }

    //
    private fun initializeAlbumHolder() {
        albumAdapter = AlbumAdapter()
        binding.vpBabyPhoto.adapter = albumAdapter
        binding.vpBabyPhoto.currentItem = viewModel.getAlbumIndex()
        viewLifecycleOwner.repeatOnStarted {
            viewModel.growthAlbumList.collect {
                albumAdapter.submitList(it)
            }
        }

//        binding.vpBabyPhoto.registerOnPageChangeCallback(object : OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                if(isSelectedByCalendar.not()){
//                    setSelectedDate(viewModel.getDateFromPosition(position))
//                }
//                isSelectedByCalendar = false
//            }
//        })

        binding.vpBabyPhoto.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
        }

        binding.vpBabyPhoto.setPageTransformer(pageTransformer)

        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.vpBabyPhoto.addItemDecoration(itemDecoration)
    }

    private fun collectSelectedAlbum() {
        repeatOnStarted {
            viewModel.selectedAlbum.collect {
                binding.vpBabyPhoto.doOnPreDraw {
                    binding.vpBabyPhoto.currentItem = viewModel.getAlbumIndex()
                }
            }
        }
    }

    private fun setSelectedDate(date: LocalDate) {
        binding.wcvAlbumCalendar.notifyDateChanged(selectedDate)
        selectedDate = date
        binding.wcvAlbumCalendar.notifyDateChanged(date)
        if (isDateInit.not()) {
            binding.wcvAlbumCalendar.scrollToDate(LocalDate.now().minusDays(3))
            isDateInit = true
        }else {
            binding.wcvAlbumCalendar.smoothScrollToDate(date.minusDays(3))
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