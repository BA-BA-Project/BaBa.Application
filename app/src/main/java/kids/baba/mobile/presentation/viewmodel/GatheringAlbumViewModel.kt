package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.GetAllAlbumsUseCase
import kids.baba.mobile.presentation.event.GatheringAlbumEvent
import kids.baba.mobile.presentation.mapper.toPresentation
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
class GatheringAlbumViewModel @Inject constructor(
    private val getAllAlbumsUseCase: GetAllAlbumsUseCase
) : ViewModel() {

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


    fun initAlbum() = viewModelScope.launch {
        // 아기 더미 데이터
        Log.e("GatheringViewModel", "initCalled")

        getAllAlbums()
        val oldestAlbumYear = if (allAlbumListState.value.isNotEmpty()) {
            allAlbumListState.value[0].date.year
        } else {
            thisYear
        }

        // 현재 year 에서부터 1년 씩 줄이면서 앨범 필터링
        while (thisYear >= oldestAlbumYear) {
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

    private suspend fun getAllAlbums() {
        val baby = EncryptedPrefs.getBaby(PrefsKey.BABY_KEY)
        val tempList: MutableList<AlbumUiModel> = mutableListOf()

        when (val result = getAllAlbumsUseCase(id = baby.babyId)) {
            is Result.Success -> {
                result.data.forEach {
                    tempList.add(it.toPresentation(false)) // false is meaningless value
                }
                _allAlbumListState.value = tempList
            }
            is Result.NetworkError -> {
                _eventFlow.emit(GatheringAlbumEvent.ShowSnackBar(R.string.baba_network_failed))
            }
            else -> {
                _eventFlow.emit(GatheringAlbumEvent.ShowSnackBar(R.string.baba_network_failed))
            }
        }


    }

}