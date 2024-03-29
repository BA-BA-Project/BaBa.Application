package kids.baba.mobile.presentation.view.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
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
import kids.baba.mobile.presentation.event.AlbumConfigEvent
import kids.baba.mobile.presentation.event.GrowthAlbumEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.helper.CameraPermissionRequester
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.util.notification.DownLoadNotificationManager
import kids.baba.mobile.presentation.view.HorizontalMarginItemDecoration
import kids.baba.mobile.presentation.view.bottomsheet.AlbumConfigBottomSheet
import kids.baba.mobile.presentation.view.bottomsheet.BabyListBottomSheet
import kids.baba.mobile.presentation.view.bottomsheet.BabyListBottomSheet.Companion.SELECTED_BABY_ID_KEY
import kids.baba.mobile.presentation.view.dialog.AlbumDetailDialog
import kids.baba.mobile.presentation.view.dialog.AlbumDetailDialog.Companion.SELECTED_ALBUM_KEY
import kids.baba.mobile.presentation.view.film.FilmActivity
import kids.baba.mobile.presentation.viewmodel.GrowthAlbumViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class GrowthAlbumFragment : Fragment() {
    private var _binding: FragmentGrowthalbumBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    val viewModel: GrowthAlbumViewModel by viewModels()

    private lateinit var albumAdapter: AlbumAdapter

    private val albumDateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern("yy-MM-dd")
    }
    private var selectedDate = LocalDate.now()

    private val permissionRequester = CameraPermissionRequester(this, ::connect, ::noPermission)

    @Inject
    lateinit var downLoadNotificationManager: DownLoadNotificationManager

    private fun connect() {
        FilmActivity.startActivity(
            requireContext(),
            viewModel.growthAlbumState.value.selectedDate.toString()
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.initBabyAndAlbum(selectedDate)
    }

    private fun noPermission() {
        Log.e("GrowthAlbumFragment", "noPermission()")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        initializeAlbumHolder()
        setCalendar()
        collectUiState()
        collectEvent()
    }

    private fun setBinding() {
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is GrowthAlbumEvent.ShowSnackBar -> showSnackBar(event.message)
                    is GrowthAlbumEvent.ShowBabyList -> showBabyList()
                    is GrowthAlbumEvent.ShowAlbumConfig -> showAlbumConfig()
                    is GrowthAlbumEvent.ShowAlbumDetail -> showAlbumDetailDialog()
                    is GrowthAlbumEvent.MoveBabyManagement -> {
                        val action = GrowthAlbumFragmentDirections.actionGrowthAlbumFragmentToMyPageFragment(true)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun showSnackBar(@StringRes message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }


    private fun showAlbumConfig() {
        val bundle = Bundle()
        bundle.putParcelable(
            AlbumConfigBottomSheet.NOW_ALBUM_KEY,
            viewModel.growthAlbumState.value.selectedAlbum
        )
        val bottomSheet = AlbumConfigBottomSheet { event ->
            when (event) {
                is AlbumConfigEvent.DeleteAlbum -> viewModel.initBabyAndAlbum(selectedDate)
                is AlbumConfigEvent.ShowDownSuccessNotification -> {
                    downLoadNotificationManager.showNotification(event.uri)
                }
                else -> {}
            }
        }
        bottomSheet.arguments = bundle
        bottomSheet.show(childFragmentManager, AlbumConfigBottomSheet.TAG)
    }


    private fun showAlbumDetailDialog() {
        val nowAlbum = viewModel.growthAlbumState.value.selectedAlbum
        if (nowAlbum.contentId != null) {
            val albumDetailDialog = AlbumDetailDialog(
                dismissLister = {
                    viewModel.initBabyAndAlbum(selectedDate)
                },
                deleteListener = {}
            )
            val bundle = Bundle()
            bundle.putParcelable(SELECTED_ALBUM_KEY, nowAlbum)
            albumDetailDialog.arguments = bundle
            albumDetailDialog.show(childFragmentManager, AlbumDetailDialog.TAG)
        }
    }



    private fun showBabyList() {
        val bundle = Bundle()
        bundle.putString(
            SELECTED_BABY_ID_KEY,
            viewModel.growthAlbumState.value.selectedBaby.babyId
        )
        val bottomSheet = BabyListBottomSheet(
            itemClick = { baby ->
                viewModel.selectBaby(baby, selectedDate)
            },
            moveBabyManagement = {
                viewModel.moveBabyManagement()
            }
        )
        bottomSheet.arguments = bundle
        bottomSheet.show(childFragmentManager, BabyListBottomSheet.TAG)
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
                var hasAlbum = false
                this.day = day
                bind.date = day.date
                bind.selected = selectedDate == day.date
                bind.formatter = formatter
                val growthAlbumList = viewModel.growthAlbumState.value.growthAlbumList

                if (growthAlbumList.isNotEmpty()) {
                    if (day.date.month == selectedDate.month) {
                        val idx = day.date.dayOfMonth - 1
                        if (idx <= growthAlbumList.lastIndex && growthAlbumList[idx].contentId != null) {
                            hasAlbum = true
                        }
                    }
                }
                bind.hasAlbum = hasAlbum
                view.isClickable = day.date.isAfter(LocalDate.now()).not()
                var textColor = if (view.isClickable) R.color.text_0 else R.color.text_3
                bind.tvWeekday.setTextColor(requireContext().getColor(textColor))
                if (hasAlbum) {
                    textColor = R.color.white
                    bind.tvDate.setBackgroundResource(R.drawable.bg_day_has_album)
                } else {
                    bind.tvDate.background = null
                }
                bind.tvDate.setTextColor(requireContext().getColor(textColor))
            }
        }

        binding.wcvAlbumCalendar.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
        }

        var currentMonth = YearMonth.now()

        binding.wcvAlbumCalendar.weekScrollListener = { weekDays ->
            val firstDate = weekDays.days.first().date
            val lastDate = weekDays.days.last().date
            currentMonth = if (lastDate.month != currentMonth.month) {
                YearMonth.of(lastDate.year, lastDate.month)
            } else if (firstDate.month != currentMonth.month) {
                YearMonth.of(firstDate.year, firstDate.month)
            } else {
                currentMonth
            }
            binding.tvDate.text =
                getString(R.string.calendar_date, currentMonth.year, currentMonth.monthValue)
        }

        binding.wcvAlbumCalendar.setup(
            currentMonth.minusMonths(24).atStartOfMonth(),
            LocalDate.now(),
            firstDayOfWeekFromLocale()
        )
        binding.wcvAlbumCalendar.scrollPaged = false
        binding.wcvAlbumCalendar.scrollToDate(LocalDate.now().minusDays(3))


    }


    private fun collectUiState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.growthAlbumState.collectLatest { state ->
                val growthAlbumList = state.growthAlbumList
                val selectedDate = state.selectedDate
                val selectedAlbum = state.selectedAlbum
                val selectedBaby = state.selectedBaby

                setViewPagerItem(growthAlbumList)
                setAlbum(selectedBaby, selectedAlbum)
                setSelectedDate(selectedDate)
            }
        }
    }

    private fun setAlbum(baby: BabyUiModel, album: AlbumUiModel) {
        @StringRes
        val toDoMessage = if (album.contentId != null) {
            R.string.add_like_and_comment
        } else {
            if (album.isMyBaby) {
                R.string.record_album
            } else {
                R.string.no_albums_recorded
            }
        }
        binding.tvDoSome.setText(toDoMessage)

        if (album.date == LocalDate.now()) {
            binding.tvAlbumDate.setText(R.string.today)
        } else {
            binding.tvAlbumDate.text = album.date.format(albumDateTimeFormatter)
        }

        binding.tvAlbumTitle.text = if (album.contentId != null) {
            album.title
        } else if (album.date == LocalDate.now()) {
            String.format(getString(R.string.today_album_title), baby.name)
        } else {
            String.format(getString(R.string.past_album_title), baby.name)
        }
    }

    private fun setViewPagerItem(growthAlbumList: List<AlbumUiModel>) {
        albumAdapter.submitListWithCallback(
            growthAlbumList
        ) {
            binding.wcvAlbumCalendar.notifyCalendarChanged()
            binding.vpBabyPhoto.currentItem = viewModel.getAlbumIndex()
        }
    }



    private fun initializeAlbumHolder() {
        albumAdapter = AlbumAdapter(
            likeClick = {
                viewModel.likeAlbum(it)
            },
            createAlbum = {
                // 권한 허용
                permissionRequester.checkPermissions(requireContext())
            },
            albumClick = {
                showAlbumDetailDialog()
            }
        )
        binding.vpBabyPhoto.adapter = albumAdapter

        binding.vpBabyPhoto.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            var isUserScrolling = false
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    isUserScrolling = true
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    if (isUserScrolling) {
                        viewModel.selectDateFromPosition(binding.vpBabyPhoto.currentItem)
                    }
                    isUserScrolling = false
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (isUserScrolling.not() && viewModel.getAlbumIndex() != position) {
                    binding.vpBabyPhoto.currentItem = viewModel.getAlbumIndex()
                }
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

    private fun setSelectedDate(date: LocalDate) {
        if (date != selectedDate) {
            binding.wcvAlbumCalendar.notifyDateChanged(selectedDate)
            selectedDate = date
            binding.wcvAlbumCalendar.notifyDateChanged(date)
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