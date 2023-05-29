package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.GetAlbumsFromBabyIdUseCase
import kids.baba.mobile.domain.usecase.GetBabiesUseCase
import kids.baba.mobile.domain.usecase.LikeAlbumUseCase
import kids.baba.mobile.presentation.event.GrowthAlbumEvent
import kids.baba.mobile.presentation.mapper.toDomain
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GrowthAlbumViewModel @Inject constructor(
    private val getAlbumsFromBabyIdUseCase: GetAlbumsFromBabyIdUseCase,
    private val getBabiesUseCase: GetBabiesUseCase,
    private val likeAlbumUseCase: LikeAlbumUseCase
) : ViewModel() {
    private val _growthAlbumState = MutableStateFlow(GrowthAlbumState())
    val growthAlbumState = _growthAlbumState.asStateFlow()

    private val _eventFlow = MutableEventFlow<GrowthAlbumEvent>()
    val eventFlow = _eventFlow.asEventFlow()


    private suspend fun loadAlbum(selectedBaby: BabyUiModel, date: LocalDate) {
        val nowYear = date.year
        val nowMonth = date.monthValue
        val startDate = LocalDate.of(nowYear, nowMonth, 1)
        val size = if (nowMonth == LocalDate.now().monthValue) {
            LocalDate.now().dayOfMonth
        } else {
            date.lengthOfMonth()
        }
        val thisMonthAlbumList = MutableList(size) { idx ->
            AlbumUiModel(date = startDate.plusDays(idx.toLong()), isMyBaby = selectedBaby.isMyBaby)
        }

        when (val result = getAlbumsFromBabyIdUseCase.getMonthAlbum(selectedBaby.babyId, nowYear, nowMonth)) {
            is Result.Success -> {
                val albumList = result.data
                albumList.forEach {
                    val day = it.date.dayOfMonth
                    thisMonthAlbumList[day - 1] = it.toPresentation(selectedBaby.isMyBaby)
                }
                _growthAlbumState.update {
                    it.copy(
                        selectedBaby = selectedBaby,
                        growthAlbumList = thisMonthAlbumList,
                        selectedDate = date,
                        selectedAlbum = thisMonthAlbumList[date.dayOfMonth - 1]
                    )
                }
            }

            is Result.NetworkError -> _eventFlow.emit(GrowthAlbumEvent.ShowSnackBar(R.string.baba_network_failed))
            else -> _eventFlow.emit(GrowthAlbumEvent.ShowSnackBar(R.string.baba_get_album_failed))
        }
    }

    fun getAlbumIndex(): Int {
        val albumState = growthAlbumState.value
        val albumList = albumState.growthAlbumList
        val selectedAlbum = albumState.selectedAlbum
        return albumList.indexOf(selectedAlbum)
    }

    fun selectDate(date: LocalDate) {
        val albumState = growthAlbumState.value
        if (albumState.selectedDate.month != date.month) {
            viewModelScope.launch {
                loadAlbum(growthAlbumState.value.selectedBaby,date)
            }
        } else {
            var selectedAlbum = albumState.selectedAlbum
            if (albumState.growthAlbumList.isNotEmpty()) {
                selectedAlbum = albumState.growthAlbumList[date.dayOfMonth - 1]
            }
            _growthAlbumState.update {
                it.copy(
                    selectedDate = date,
                    selectedAlbum = selectedAlbum
                )
            }
        }
    }

    fun selectDateFromPosition(position: Int) {
        _growthAlbumState.update {
            it.copy(
                selectedAlbum = it.growthAlbumList[position],
                selectedDate = it.growthAlbumList[position].date
            )
        }
    }

    fun selectBaby(baby: BabyUiModel, selectedDate: LocalDate) = viewModelScope.launch {
        loadAlbum(baby, selectedDate)
        _growthAlbumState.update {
            it.copy(
                selectedBaby = baby.copy(selected = true)
            )
        }
        EncryptedPrefs.putBaby(PrefsKey.BABY_KEY, baby.toDomain())
    }


    fun initBabyAndAlbum(date: LocalDate) = viewModelScope.launch {
        val babyId = runCatching { EncryptedPrefs.getBaby(PrefsKey.BABY_KEY).babyId }.getOrNull()
        when (val result = getBabiesUseCase()) {
            is Result.Success -> {
                val myBaby = result.data.myBaby
                val others = result.data.others

                val selectedBaby =
                    if (babyId == null) {
                        myBaby.first().toPresentation()
                    } else {
                        myBaby.firstOrNull { it.babyId == babyId }?.toPresentation()
                            ?: others.firstOrNull { it.babyId == babyId }
                                ?.toPresentation()
                            ?: myBaby.first().toPresentation()
                    }
                EncryptedPrefs.putBaby(PrefsKey.BABY_KEY, selectedBaby.toDomain())
                loadAlbum(selectedBaby.copy(selected = true), date)
            }

            is Result.NetworkError -> _eventFlow.emit(GrowthAlbumEvent.ShowSnackBar(R.string.baba_network_failed))
            else -> _eventFlow.emit(GrowthAlbumEvent.ShowSnackBar(R.string.baba_get_babies_failed))
        }
    }

    fun likeAlbum(album: AlbumUiModel) = viewModelScope.launch {
        if (album.contentId != null) {
            when (val result = likeAlbumUseCase(
                growthAlbumState.value.selectedBaby.babyId,
                album.contentId
            )) {
                is Result.Success -> {
                    var selectedAlbum = growthAlbumState.value.selectedAlbum
                    val growthAlbumList = growthAlbumState.value.growthAlbumList.map {
                        if (it == album) {
                            selectedAlbum = album.copy(like = result.data)
                            selectedAlbum
                        } else {
                            it
                        }
                    }
                    _growthAlbumState.update {
                        it.copy(
                            selectedAlbum = selectedAlbum,
                            growthAlbumList = growthAlbumList
                        )
                    }
                }

                is Result.NetworkError -> _eventFlow.emit(GrowthAlbumEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(GrowthAlbumEvent.ShowSnackBar(R.string.baba_like_album_failed))
            }
        }
    }

    fun showBabyList() = viewModelScope.launch {
        _eventFlow.emit(GrowthAlbumEvent.ShowBabyList)
    }

    fun showAlbumDetail() = viewModelScope.launch {
        _eventFlow.emit(GrowthAlbumEvent.ShowAlbumDetail)
    }

    fun showAlbumConfig() = viewModelScope.launch {
        _eventFlow.emit(GrowthAlbumEvent.ShowAlbumConfig)
    }
}