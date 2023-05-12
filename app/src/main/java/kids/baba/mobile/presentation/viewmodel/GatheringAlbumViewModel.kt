package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.event.GatheringAlbumEvent
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.ClassifiedAlbumList
import kids.baba.mobile.presentation.model.GatheringAlbumCountUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GatheringAlbumViewModel @Inject constructor() : ViewModel() {

    val TAG = "GatheringViewModel"

    private val _eventFlow = MutableEventFlow<GatheringAlbumEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    // 전체 앨범
    private val _allAlbumListState: MutableStateFlow<List<AlbumUiModel>> = MutableStateFlow(emptyList())
    val allAlbumListState = _allAlbumListState.asStateFlow()

    // 년도별 가장 최근 Album 과 해당 년도의 앨범 수 - RepresentativeAlbumUiModel 은 photo 와 date 만.
    private val _recentYearAlbumListState: MutableStateFlow<List<GatheringAlbumCountUiModel>> = MutableStateFlow(
        emptyList()
    )
    val recentYearAlbumListState = _recentYearAlbumListState.asStateFlow()

    // 월별 가장 최근 Album 과 해당 월의 앨범 수 - RepresentativeAlbumUiModel 은 photo 와 date 만.
    private val _recentMonthAlbumListState: MutableStateFlow<List<GatheringAlbumCountUiModel>> = MutableStateFlow(
        emptyList()
    )
    val recentMonthAlbumListState = _recentMonthAlbumListState.asStateFlow()

    // 년도별로 분류한 모든 앨범 list
    private val _yearAlbumListState: MutableStateFlow<List<ClassifiedAlbumList>> =
        MutableStateFlow(emptyList())
    private val yearAlbumListState = _yearAlbumListState.asStateFlow()

    // 월별로 분류한 모든 album list
    private val _monthAlbumListState: MutableStateFlow<List<ClassifiedAlbumList>> =
        MutableStateFlow(emptyList())
    private val monthAlbumListState = _monthAlbumListState.asStateFlow()

    private var thisYear = LocalDate.now().year
    private var thisMonth = LocalDate.now().monthValue

    private var tempYearAlbumList: MutableList<ClassifiedAlbumList> = mutableListOf()
    private var tempMonthAlbumList: MutableList<ClassifiedAlbumList> = mutableListOf()

    private var tempYearAlbumCountList: MutableList<GatheringAlbumCountUiModel> =
        mutableListOf()
    private var tempMonthAlbumCountList: MutableList<GatheringAlbumCountUiModel> =
        mutableListOf()

    init {
        initAlbum()
    }

    private fun initAlbum() = viewModelScope.launch {
        // 아기 더미 데이터

        getAlbum()

        // 현재 year 에서부터 1년 씩 줄이면서 앨범 필터링
        while (thisYear >= LAUNCHING_YEAR) {
            val tempRecentYearAlbum =
                allAlbumListState.value.lastOrNull { it.date.year == thisYear }?.toRepresentativeAlbum()

            val yearAlbumCount = allAlbumListState.value.count { it.date.year == thisYear }

            val tempYearAlbums = allAlbumListState.value.filter { it.date.year == thisYear }
            if (tempRecentYearAlbum != null) {
                tempYearAlbumCountList.add(GatheringAlbumCountUiModel(tempRecentYearAlbum, yearAlbumCount))
                tempYearAlbumList.add(ClassifiedAlbumList(tempYearAlbums))
            }
            while (thisMonth >= 1) {
                val tempRecentMonthAlbum = allAlbumListState.value.lastOrNull {
                    it.date.year == thisYear && it.date.monthValue == thisMonth
                }?.toRepresentativeAlbum()

                val monthAlbumCount =
                    allAlbumListState.value.count { it.date.year == thisYear && it.date.monthValue == thisMonth }

                val tempMonthAlbums =
                    allAlbumListState.value.filter { it.date.year == thisYear && it.date.monthValue == thisMonth }
                if (tempRecentMonthAlbum != null) {
                    tempMonthAlbumCountList.add(GatheringAlbumCountUiModel(tempRecentMonthAlbum, monthAlbumCount))
                    tempMonthAlbumList.add(ClassifiedAlbumList(tempMonthAlbums))

                }
                thisMonth -= 1
            }
            thisYear -= 1
            thisMonth = 12
        }

        _recentYearAlbumListState.value = tempYearAlbumCountList
        _recentMonthAlbumListState.value = tempMonthAlbumCountList

        _yearAlbumListState.value = tempYearAlbumList
        _monthAlbumListState.value = tempMonthAlbumList

        testLog()

    }

    // 머지 허가되면 바로 지울 testLog. PR 확인하는 팀원들을 위한 확인용
    private fun testLog() {
        Log.e(TAG, "recentYearAlbumList : ${recentYearAlbumListState.value} \n\n")
        Log.e(TAG, "recentMonthAlbumList: ${recentMonthAlbumListState.value} \n\n")
        Log.e(
            TAG, "yearAlbumList - 2023: ${yearAlbumListState.value[0]} \n\n" +
                    "2022: ${yearAlbumListState.value[1]}  \n\n"
        )
        Log.e(
            TAG, "monthAlbumList - 2023-5 ${monthAlbumListState.value[0]}------------- \n\n" +
                    "2023-4 : ${monthAlbumListState.value[1]}-------------\n\n" +
                    "2023-3 : ${monthAlbumListState.value[2]}-------------\n\n" +
                    "2023-2 : ${monthAlbumListState.value[3]}-------------\n\n" +
                    "2023-1 : ${monthAlbumListState.value[4]}-------------\n\n"
        )
    }

    fun showClassifiedAllAlbumsByMonth(itemId: Int) = viewModelScope.launch {
        _eventFlow.emit(
            GatheringAlbumEvent.GoToClassifiedAllAlbum(
                itemId = itemId,
                classifiedAlbumList = monthAlbumListState.value[itemId],
                fromMonth = true
            )
        )
    }

    fun showClassifiedAllAlbumsByYear(itemId: Int) = viewModelScope.launch {
        _eventFlow.emit(
            GatheringAlbumEvent.GoToClassifiedAllAlbum(
                itemId = itemId,
                classifiedAlbumList = yearAlbumListState.value[itemId],
                fromMonth = false
            )
        )
    }

    // 더미 데이터 - 굳이 읽어보실 필요 없습니다.
    private fun getAlbum() {
        _allAlbumListState.value = listOf(
            // 2022 년
            AlbumUiModel(
                100,
                "name2022-1",
                "엄마",
                LocalDate.of(2022, 1, 1),
                "2022-1",
                false,
                "https://i.pravatar.cc/300",
                "CARD_BASIC_1",
                true
            ),
            AlbumUiModel(
                101,
                "name2022-2",
                "아빠",
                LocalDate.of(2022, 2, 1),
                "2022-2",
                true,
                "https://i.pravatar.cc/301",
                "CARD_CLOUD_1",
                true
            ),
            AlbumUiModel(
                102,
                "name2022-3",
                "엄마",
                LocalDate.of(2022, 2, 2),
                "2022-3",
                false,
                "https://i.pravatar.cc/302",
                "CARD_TOY_1",
                true
            ),
            AlbumUiModel(
                103,
                "name2022-4",
                "아빠",
                LocalDate.of(2022, 3, 1),
                "2022-4",
                true,
                "https://i.pravatar.cc/303",
                "CARD_BASIC_1",
                false
            ),
            AlbumUiModel(
                104,
                "name2022-5",
                "엄마",
                LocalDate.of(2022, 3, 2),
                "2022-5",
                false,
                "https://i.pravatar.cc/304",
                "CARD_SNOWFLOWER_2",
                true
            ),
            AlbumUiModel(
                105,
                "name2022-6",
                "엄마",
                LocalDate.of(2022, 3, 3),
                "2022-6",
                true,
                "https://i.pravatar.cc/305",
                "CARD_LINE_1",
                false
            ),
            AlbumUiModel(
                106,
                "name2022-7",
                "이모",
                LocalDate.of(2022, 4, 1),
                "2022-7",
                false,
                "https://i.pravatar.cc/306",
                "CARD_CHECK_1",
                true
            ),
            AlbumUiModel(
                107,
                "name2022-8",
                "삼촌",
                LocalDate.of(2022, 4, 2),
                "2022-8",
                true,
                "https://i.pravatar.cc/307",
                "CARD_CHECK_2",
                false
            ),
            AlbumUiModel(
                108,
                "name2022-9",
                "삼촌",
                LocalDate.of(2022, 4, 3),
                "2022-9",
                true,
                "https://i.pravatar.cc/308",
                "CARD_CHECK_2",
                false
            ),
            AlbumUiModel(
                109,
                "name2022-10",
                "엄마",
                LocalDate.of(2022, 4, 4),
                "2022-10",
                false,
                "https://i.pravatar.cc/300",
                "CARD_BASIC_1",
                true
            ),
            AlbumUiModel(
                110,
                "name2022-11",
                "아빠",
                LocalDate.of(2022, 5, 1),
                "2022-11",
                true,
                "https://i.pravatar.cc/301",
                "CARD_CLOUD_1",
                true
            ),
            AlbumUiModel(
                111,
                "name2022-12",
                "엄마",
                LocalDate.of(2022, 5, 2),
                "2022-12",
                false,
                "https://i.pravatar.cc/302",
                "CARD_TOY_1",
                true
            ),
            AlbumUiModel(
                112,
                "name2022-13",
                "아빠",
                LocalDate.of(2022, 5, 3),
                "2022-13",
                true,
                "https://i.pravatar.cc/303",
                "CARD_BASIC_1",
                false
            ),
            AlbumUiModel(
                113,
                "name2022-14",
                "엄마",
                LocalDate.of(2022, 5, 4),
                "2022-14",
                false,
                "https://i.pravatar.cc/304",
                "CARD_SNOWFLOWER_2",
                true
            ),
            AlbumUiModel(
                114,
                "name2022-15",
                "엄마",
                LocalDate.of(2022, 5, 5),
                "2022-15",
                true,
                "https://i.pravatar.cc/305",
                "CARD_LINE_1",
                false
            ),
            AlbumUiModel(
                115,
                "name2022-16",
                "이모",
                LocalDate.of(2022, 6, 1),
                "2022-16",
                false,
                "https://i.pravatar.cc/306",
                "CARD_CHECK_1",
                true
            ),
            AlbumUiModel(
                116,
                "name2022-17",
                "삼촌",
                LocalDate.of(2022, 6, 2),
                "2022-17",
                true,
                "https://i.pravatar.cc/307",
                "CARD_CHECK_2",
                false
            ),
            AlbumUiModel(
                117,
                "name2022-18",
                "삼촌",
                LocalDate.of(2022, 6, 3),
                "2022-18",
                true,
                "https://i.pravatar.cc/308",
                "CARD_CHECK_2",
                false
            ),
            // 2023년
            AlbumUiModel(
                100,
                "name2023-1",
                "엄마",
                LocalDate.of(2023, 1, 1),
                "2023-1",
                false,
                "https://i.pravatar.cc/300",
                "CARD_BASIC_1",
                true
            ),
            AlbumUiModel(
                101,
                "name20232",
                "아빠",
                LocalDate.of(2023, 2, 1),
                "2023-2",
                true,
                "https://i.pravatar.cc/301",
                "CARD_CLOUD_1",
                true
            ),
            AlbumUiModel(
                102,
                "name20233",
                "엄마",
                LocalDate.of(2023, 2, 2),
                "2023-3",
                false,
                "https://i.pravatar.cc/302",
                "CARD_TOY_1",
                true
            ),
            AlbumUiModel(
                103,
                "name20234",
                "아빠",
                LocalDate.of(2023, 3, 1),
                "2023-4",
                true,
                "https://i.pravatar.cc/303",
                "CARD_BASIC_1",
                false
            ),
            AlbumUiModel(
                104,
                "name20235",
                "엄마",
                LocalDate.of(2023, 3, 2),
                "2023-5",
                false,
                "https://i.pravatar.cc/304",
                "CARD_SNOWFLOWER_2",
                true
            ),
            AlbumUiModel(
                105,
                "name20236",
                "엄마",
                LocalDate.of(2023, 3, 3),
                "2023-6",
                true,
                "https://i.pravatar.cc/305",
                "CARD_LINE_1",
                false
            ),
            AlbumUiModel(
                106,
                "name20237",
                "이모",
                LocalDate.of(2023, 4, 1),
                "2023-7",
                false,
                "https://i.pravatar.cc/306",
                "CARD_CHECK_1",
                true
            ),
            AlbumUiModel(
                107,
                "name20238",
                "삼촌",
                LocalDate.of(2023, 4, 2),
                "2023-8",
                true,
                "https://i.pravatar.cc/307",
                "CARD_CHECK_2",
                false
            ),
            AlbumUiModel(
                108,
                "name20239",
                "삼촌",
                LocalDate.of(2023, 4, 3),
                "2023-9",
                true,
                "https://i.pravatar.cc/308",
                "CARD_CHECK_2",
                false
            ),
            AlbumUiModel(
                109,
                "name202310",
                "엄마",
                LocalDate.of(2023, 4, 4),
                "2023-10",
                false,
                "https://i.pravatar.cc/300",
                "CARD_BASIC_1",
                true
            ),
            AlbumUiModel(
                110,
                "name202311",
                "아빠",
                LocalDate.of(2023, 5, 1),
                "2023-11",
                true,
                "https://i.pravatar.cc/301",
                "CARD_CLOUD_1",
                true
            ),
            AlbumUiModel(
                111,
                "name202312",
                "엄마",
                LocalDate.of(2023, 5, 2),
                "2023-12",
                false,
                "https://i.pravatar.cc/302",
                "CARD_TOY_1",
                true
            ),
            AlbumUiModel(
                112,
                "name202313",
                "아빠",
                LocalDate.of(2023, 5, 3),
                "2023-13",
                true,
                "https://i.pravatar.cc/303",
                "CARD_BASIC_1",
                false
            ),

            )
    }

    companion object {
        const val LAUNCHING_YEAR = 2022
    }
}