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


    private fun loadAlbum(date: LocalDate) = viewModelScope.launch {
        val nowYear = date.year
        val nowMonth = date.monthValue
        val startDate = LocalDate.of(nowYear, nowMonth, 1)
        val size = if (nowMonth == LocalDate.now().monthValue) {
            LocalDate.now().dayOfMonth
        } else {
            date.lengthOfMonth()
        }
        val selectedBaby = growthAlbumState.value.selectedBaby
        val thisMonthAlbumList = MutableList(size) { idx ->
            AlbumUiModel(date = startDate.plusDays(idx.toLong()), isMyBaby = selectedBaby.isMyBaby)
        }

        when (val result = getAlbumsFromBabyIdUseCase(selectedBaby.babyId, nowYear, nowMonth)){
            is Result.Success -> {
                val albumList = result.data
                albumList.forEach {
                    val day = it.date.dayOfMonth
                    thisMonthAlbumList[day - 1] = it.toPresentation(selectedBaby.isMyBaby)
                }
                _growthAlbumState.update {
                    it.copy(
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
            loadAlbum(date)
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

    fun selectBaby(baby: BabyUiModel, selectedDate: LocalDate) {
        _growthAlbumState.update {
            it.copy(
                selectedBaby = baby.copy(selected = true)
            )
        }
        EncryptedPrefs.putBaby(PrefsKey.BABY_KEY, baby.toDomain())
        loadAlbum(selectedDate)
    }


    fun initBabyAndAlbum(date: LocalDate) = viewModelScope.launch {
        val babyId = runCatching { EncryptedPrefs.getBaby(PrefsKey.BABY_KEY).babyId }.getOrNull()
        getBabiesUseCase.getBabies().collect { babyResponse ->
            val selectedBaby =
                if (babyId == null) {
                    babyResponse.myBaby.first().toPresentation(true)
                } else {
                    babyResponse.myBaby.firstOrNull { it.babyId == babyId }?.toPresentation(true)
                        ?: babyResponse.others.firstOrNull { it.babyId == babyId }
                            ?.toPresentation(false)
                        ?: babyResponse.myBaby.first().toPresentation(true)
                }
            EncryptedPrefs.putBaby(PrefsKey.BABY_KEY, selectedBaby.toDomain())

            _growthAlbumState.update { growthAlbumState ->
                growthAlbumState.copy(selectedBaby = selectedBaby.copy(selected = true))
            }
        }
        loadAlbum(date)
    }

    fun likeAlbum(album: AlbumUiModel) = viewModelScope.launch {
        if (album.contentId != null) {
            likeAlbumUseCase.like(
                growthAlbumState.value.selectedBaby.babyId,
                album.contentId.toString()
            ).collect { likeResponse ->
                var selectedAlbum = growthAlbumState.value.selectedAlbum
                val growthAlbumList = growthAlbumState.value.growthAlbumList.map {
                    if (it == album) {
                        selectedAlbum = album.copy(like = likeResponse.isLiked)
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
        }
    }

}