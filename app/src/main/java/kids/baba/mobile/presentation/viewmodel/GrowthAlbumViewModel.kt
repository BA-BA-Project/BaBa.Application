package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.error.BabyNotFoundException
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.usecase.GetAlbumsFromBabyIdUseCase
import kids.baba.mobile.domain.usecase.GetBabiesUseCase
import kids.baba.mobile.domain.usecase.LikeAlbumUseCase
import kids.baba.mobile.presentation.mapper.toDomain
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

//TODO UISTATE를 하나의 클래스에서 관리하고 COLLECT를 해야하나 싶음.
//TODO 각자 COLLECT를 진행하니 별도의 코루틴으로 동작하여 가끔 제대로된 값을 가져오지 못하는 경우가 있는것 같음



@HiltViewModel
class GrowthAlbumViewModel @Inject constructor(
    private val getAlbumsFromBabyIdUseCase: GetAlbumsFromBabyIdUseCase,
    private val getBabiesUseCase: GetBabiesUseCase,
    private val likeAlbumUseCase: LikeAlbumUseCase
) : ViewModel() {
    private val _growthAlbumState =
        MutableStateFlow<GrowthAlbumState>(GrowthAlbumState.Loading)
    val growthAlbumState = _growthAlbumState

    private val _selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _growthAlbumList = MutableStateFlow<List<AlbumUiModel>>(emptyList())
    val growthAlbumList = _growthAlbumList.asStateFlow()

    private val _selectedAlbum = MutableStateFlow(AlbumUiModel(date = LocalDate.now()))
    val selectedAlbum = _selectedAlbum.asStateFlow()

    private val _selectedBaby = MutableStateFlow(BabyUiModel())
    val selectedBaby = _selectedBaby.asStateFlow()

    init {
        loadBaby()
    }

    private fun loadAlbum(date: LocalDate) = viewModelScope.launch {
        val nowYear = date.year
        val nowMonth = date.monthValue
        val startDate = LocalDate.of(nowYear,nowMonth,1)
        val size = if(date == LocalDate.now()) {
            date.dayOfMonth
        } else {
            date.lengthOfMonth()
        }
        val thisMonthAlbumList = MutableList(size){idx ->
            AlbumUiModel(date = startDate.plusDays(idx.toLong()))
        }
        getAlbumsFromBabyIdUseCase.getAlbumsFromBabyId(
            selectedBaby.value.babyId,
            nowYear,
            nowMonth
        ).collect { albumList ->
            albumList.album.forEach {
                val day = it.date.dayOfMonth
                thisMonthAlbumList[day-1] = it.toPresentation()
            }
            _growthAlbumList.value = thisMonthAlbumList
            _selectedDate.value = date
            _selectedAlbum.value = _growthAlbumList.value[date.dayOfMonth - 1]
        }
    }

    fun getAlbumIndex(): Int {
        return _growthAlbumList.value.indexOf(_selectedAlbum.value)
    }

    fun selectDate(date: LocalDate) {
        if(_selectedDate.value.month != date.month){
            loadAlbum(date)
        } else {
            _selectedDate.value = date
            if(_growthAlbumList.value.isNotEmpty()){
                _selectedAlbum.value = _growthAlbumList.value[date.dayOfMonth - 1]
            }
        }
    }

    fun selectDateFromPosition(position: Int) {
        _selectedAlbum.value = _growthAlbumList.value[position]
        _selectedDate.value = _growthAlbumList.value[position].date
    }

    fun selectBaby(baby: BabyUiModel) {
        _selectedBaby.value = baby
        EncryptedPrefs.putBaby(PrefsKey.BABY_KEY, baby.toDomain())
    }


    private fun loadBaby() = viewModelScope.launch {
        runCatching {
            _selectedBaby.value = EncryptedPrefs.getBaby(PrefsKey.BABY_KEY).toPresentation()
        }.getOrElse {
            if (it is BabyNotFoundException) {
                getBabiesUseCase.getBabies().collect { babyList ->
                    _selectedBaby.value = babyList.myBaby.first().toPresentation()
                }
            }
        }
        loadAlbum(LocalDate.now())
    }

    fun likeAlbum(album: AlbumUiModel) = viewModelScope.launch {
        if(album.contentId != -1){
            likeAlbumUseCase.like(selectedBaby.value.babyId, album.contentId.toString()).collect{ likeResponse ->
                _growthAlbumList.value = _growthAlbumList.value.map {
                    if(it == album){
                        _selectedAlbum.value = album.copy(like = likeResponse.isLiked)
                        selectedAlbum.value
                    }else {
                        it
                    }
                }
            }
        }
    }

}