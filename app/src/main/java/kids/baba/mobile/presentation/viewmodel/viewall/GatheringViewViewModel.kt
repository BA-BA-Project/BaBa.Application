package kids.baba.mobile.presentation.viewmodel.viewall

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GatheringViewViewModel @Inject constructor() : ViewModel() {

    val TAG = "GatheringViewModel"

    private val _allAlbumListState: MutableStateFlow<List<GatheringAlbumUiModel>> = MutableStateFlow(listOf())

    // 년도별 가장 최근 Album 과 해당 년도의 앨범 수
    private val _recentYearAlbumListState: MutableStateFlow<List<GatheringAlbumCountUiModel>> = MutableStateFlow(
        listOf()
    )

    // 월별 가장 최근 Album 과 해당 월의 앨범 수
    private val _recentMonthAlbumListState: MutableStateFlow<List<GatheringAlbumCountUiModel>> = MutableStateFlow(
        listOf()
    )

    private val _yearAlbumListState: MutableStateFlow<List<List<GatheringAlbumUiModel>>> = MutableStateFlow(listOf())
    private val _monthAlbumListState: MutableStateFlow<List<List<GatheringAlbumUiModel>>> = MutableStateFlow(listOf())


    val allAlbumListState = _allAlbumListState.asStateFlow()
    val recentYearAlbumListState = _recentYearAlbumListState.asStateFlow()
    val recentMonthAlbumListState = _recentMonthAlbumListState.asStateFlow()

    val yearAlbumListState = _yearAlbumListState.asStateFlow()
    val monthAlbumListState = _monthAlbumListState.asStateFlow()

    // 1씩 줄여나갈 것임
    private var thisYear = LocalDate.now().year
    private var thisMonth = LocalDate.now().monthValue

    private var yearAlbumCount: Int = 0
    private var monthAlbumCount: Int = 0

    private var tempYearAlbumList: MutableList<List<GatheringAlbumUiModel>> = mutableListOf()
    private var tempMonthAlbumList: MutableList<List<GatheringAlbumUiModel>> = mutableListOf()

    private var tempYearAlbumCountList: MutableList<GatheringAlbumCountUiModel> = mutableListOf()
    private var tempMonthAlbumCountList: MutableList<GatheringAlbumCountUiModel> = mutableListOf()

    init {
        initAlbum()
    }

    private fun initAlbum() = viewModelScope.launch {
        // 아기 더미 데이터

        getAlbum()

        // TODO: year 별로 가장 최신의 Album 을 넣기.
        while (thisYear >= LAUNCHING_YEAR) {
            val tempRecentYearAlbum = allAlbumListState.value.lastOrNull { it.date.year == thisYear }
            yearAlbumCount = allAlbumListState.value.count { it.date.year == thisYear }

            val tempYearAlbums = allAlbumListState.value.filter { it.date.year == thisYear }
            if (tempRecentYearAlbum != null) {
                tempYearAlbumCountList.add(GatheringAlbumCountUiModel(tempRecentYearAlbum, yearAlbumCount))
                tempYearAlbumList.add(tempYearAlbums)
            }
            while (thisMonth >= 1) {
                val tempRecentMonthAlbum =
                    allAlbumListState.value.lastOrNull { it.date.monthValue == thisMonth && it.date.year == thisYear }
                monthAlbumCount = allAlbumListState.value.count { it.date.monthValue == thisMonth && it.date.year == thisYear }

                val tempMonthAlbums = allAlbumListState.value.filter { it.date.monthValue == thisMonth && it.date.year == thisYear }
                if (tempRecentMonthAlbum != null) {
                    tempMonthAlbumCountList.add(GatheringAlbumCountUiModel(tempRecentMonthAlbum, monthAlbumCount))
                    tempMonthAlbumList.add(tempMonthAlbums)
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

        Log.e(TAG, "recentYearAlbumList : ${recentYearAlbumListState.value}")
        Log.e(TAG, "recentMonthAlbumList: ${recentMonthAlbumListState.value}")

        Log.e(TAG, "yearAlbumList - 2023: ${yearAlbumListState.value[0]} " +
                "2022: ${yearAlbumListState.value[1]}")
        Log.e(TAG, "monthAlbumList - 2023-5 ${monthAlbumListState.value[0]}-------------" +
                "2023-4 ${monthAlbumListState.value[1]}-------------" +
                "2023-3 ${monthAlbumListState.value[2]}-------------" +
                "2023-2 ${monthAlbumListState.value[3]}-------------" +
                "2023-1 ${monthAlbumListState.value[4]}-------------")

    }

    // 더미 데이터
    private fun getAlbum() {
        _allAlbumListState.value = listOf(
            // 2022 년
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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
            GatheringAlbumUiModel(
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