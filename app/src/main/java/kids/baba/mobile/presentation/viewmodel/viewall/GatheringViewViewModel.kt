package kids.baba.mobile.presentation.viewmodel.viewall

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.GatheringAllAlbumUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GatheringViewViewModel @Inject constructor() : ViewModel() {

    val TAG = "GatheringViewModel"

    // 일단 모두 타입을 All 로 해보자
    private val _allAlbumListState: MutableStateFlow<List<GatheringAllAlbumUiModel>> = MutableStateFlow(listOf())
    private val _yearAlbumListState: MutableStateFlow<MutableList<GatheringAllAlbumUiModel>> = MutableStateFlow(
        mutableListOf()
    )
    private val _monthAlbumListState: MutableStateFlow<MutableList<GatheringAllAlbumUiModel>> =
        MutableStateFlow(mutableListOf())

    private val _monthAlbumCountState: MutableStateFlow<List<Int>> = MutableStateFlow(listOf())

    val allAlbumListState = _allAlbumListState.asStateFlow()
    val yearAlbumListState = _yearAlbumListState.asStateFlow()
    val monthAlbumListState = _monthAlbumListState.asStateFlow()

    // 1씩 줄여나갈 것임
    private var thisYear = LocalDate.now().year
    private var thisMonth = LocalDate.now().monthValue

    private var tempAlbum: GatheringAllAlbumUiModel? = null
    private var tempLargeCountIdx: Int = 0
    private var tempSmallCountIdx: Int = 0

    init {
        initAlbum()
    }

    private fun initAlbum() = viewModelScope.launch {
        // 아기 더미 데이터

        getAlbum()

        // TODO: year 별로 가장 최신의 Album 을 넣기.
        while (thisYear >= LAUNCHING_YEAR) {
//            tempAlbum = allAlbumListState.value.lastOrNull{it.date.year == thisYear}
            tempLargeCountIdx = allAlbumListState.value.indexOfLast { it.date.year == thisYear }
            tempSmallCountIdx = allAlbumListState.value.indexOfFirst { it.date.year == thisYear }
            tempAlbum = allAlbumListState.value[tempLargeCountIdx]

            if (tempAlbum != null) {
                _yearAlbumListState.value.add(tempAlbum!!)
            }
            while (thisMonth >= 1) {
//                tempAlbum = allAlbumListState.value.lastOrNull{it.date.monthValue == thisMonth}
                tempLargeCountIdx = allAlbumListState.value.indexOfLast { it.date.monthValue == thisMonth }
                tempSmallCountIdx = allAlbumListState.value.indexOfFirst { it.date.monthValue == thisMonth }

                tempAlbum = allAlbumListState.value[tempLargeCountIdx]

                if (tempAlbum != null) {
                    _monthAlbumListState.value.add(tempAlbum!!)
                }
                thisMonth -= 1
            }
            thisYear -= 1
        }

        Log.e(TAG, "yearAlbumList : ${yearAlbumListState.value}")
        Log.e(TAG, "monthAlbumList: ${monthAlbumListState.value}")

    }

    // 더미 데이터
    private fun getAlbum() {
        _allAlbumListState.value = listOf(
            // 2022 년
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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
            GatheringAllAlbumUiModel(
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