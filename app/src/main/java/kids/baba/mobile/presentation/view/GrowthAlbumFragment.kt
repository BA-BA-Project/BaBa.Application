package kids.baba.mobile.presentation.view

import android.app.DatePickerDialog
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
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.view.AlbumDetailDialog.Companion.SELECTED_ALBUM_KEY
import kids.baba.mobile.presentation.view.BabyListBottomSheet.Companion.SELECTED_BABY_KEY
import kids.baba.mobile.presentation.viewmodel.GrowthAlbumViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

//TODO datePicker 대신 달력 커스터 마이징
//TODO api 연동(dummySource대신 불러온 데이터 넣기만 하면됨
// 사용한 오픈소스 달력
@AndroidEntryPoint
class GrowthAlbumFragment : Fragment() {

    private var _binding: FragmentGrowthalbumBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    val viewModel: GrowthAlbumViewModel by viewModels()

    private lateinit var albumAdapter: AlbumAdapter

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

        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                viewModel.selectDate(LocalDate.of(year, monthOfYear + 1, dayOfMonth))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        val maxDate = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        datePickerDialog.datePicker.maxDate = maxDate

        binding.tvDate.setOnClickListener {
            datePickerDialog.show()
        }

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
                bind.hasAlbum = false

                if (viewModel.growthAlbumList.value.isNotEmpty()) {
                    if (day.date.month == selectedDate.month) {
                        val idx = day.date.dayOfMonth - 1
                        if (idx <= viewModel.growthAlbumList.value.lastIndex && viewModel.growthAlbumList.value[idx].contentId != -1) {
                            bind.hasAlbum = true
                        }
                    }
                }

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
        var isViewPagerInit = false
        albumAdapter = AlbumAdapter()
        binding.vpBabyPhoto.adapter = albumAdapter
        binding.vpBabyPhoto.currentItem = viewModel.getAlbumIndex()
        viewLifecycleOwner.repeatOnStarted {
            viewModel.growthAlbumList.collect {
                albumAdapter.submitList(it)
                binding.wcvAlbumCalendar.notifyCalendarChanged()
            }
        }

        binding.vpBabyPhoto.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (isViewPagerInit) {
                    viewModel.selectDateFromPosition(position)
                }
                isViewPagerInit = true
            }
        })

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
        var selectedAlbum: AlbumUiModel? = null
        binding.tvAlbumTitle.setOnClickListener {
            val albumDetailDialog = AlbumDetailDialog()
            val bundle = Bundle()
            bundle.putParcelable(SELECTED_ALBUM_KEY, selectedAlbum)
            albumDetailDialog.arguments = bundle
            albumDetailDialog.show(childFragmentManager, AlbumDetailDialog.TAG)
        }

        repeatOnStarted {
            viewModel.selectedAlbum.collect {
                selectedAlbum = it
                binding.vpBabyPhoto.doOnPreDraw {
                    binding.vpBabyPhoto.currentItem = viewModel.getAlbumIndex()
                }
                if (selectedDate == LocalDate.now()) {
                    binding.tvAlbumDate.setText(R.string.today)
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
        } else {
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