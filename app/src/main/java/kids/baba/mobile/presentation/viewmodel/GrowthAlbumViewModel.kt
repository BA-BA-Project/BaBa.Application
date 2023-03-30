package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.GetOneAlbumUseCase
import kids.baba.mobile.domain.usecase.GetOneBabyUseCase
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GrowthAlbumViewModel @Inject constructor(
    private val getOneAlbumUseCase: GetOneAlbumUseCase,
    private val getOneBabyUseCase: GetOneBabyUseCase
) : ViewModel() {

    private val _growthAlbumState =
        MutableStateFlow<GrowthAlbumState>(GrowthAlbumState.Loading)
    val growthAlbumState = _growthAlbumState

    private val _currentDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val currentDate = _currentDate.asStateFlow()

    private val _growthAlbumList = MutableStateFlow<List<AlbumUiModel>>(emptyList())
    val growthAlbumList = _growthAlbumList.asStateFlow()

    private val _growthAlbumHash = MutableStateFlow<HashMap<LocalDate, AlbumUiModel>>(HashMap())
    private val growthAlbumHash = _growthAlbumHash.asStateFlow()

    private val _selectedAlbum = MutableStateFlow<AlbumUiModel?>(null)
    val selectedAlbum = _selectedAlbum.asStateFlow()

    private val tempDate = LocalDate.now()
    private val tempAlbumList = List(30) { idx ->
        if (idx % 3 == 0) {
            null
        } else {
            AlbumUiModel(
                contentId = idx,
                name = "이호성$idx",
                relation = "아빠$idx",
                date = tempDate.plusDays(idx.toLong()),
                title = "제목$idx",
                like = idx % 2 == 0,
                photo = "https://www.shutterstock.com/image-photo/cute-little-african-american-infant-600w-1937038210.jpg",
                cardStyle = "TEST"
            )
        }

    }

    init {
        loadAlbum()
    }

    private fun loadAlbum() = viewModelScope.launch {
        val newHashMap = HashMap<LocalDate,AlbumUiModel>()
        tempAlbumList.filterNotNull().forEach {
            newHashMap[it.date] = it
        }
        _growthAlbumHash.value = newHashMap
        _selectedAlbum.value = growthAlbumList.value[0]

//        getOneAlbumUseCase.getOneAlbum(id).catch {
//            _growthAlbumState.value = GrowthAlbumState.Error(it)
//        }.collect {
//            _growthAlbumState.value = GrowthAlbumState.SuccessAlbum(it.album)
//        }
    }

    fun selectAlbum(index: Int) {
        _selectedAlbum.value = growthAlbumList.value[index]
    }

    fun selectDate(date: LocalDate) {
        _currentDate.value = date
    }

    fun loadBaby() = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        getOneBabyUseCase.getOneBaby().catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect {
            _growthAlbumState.value = GrowthAlbumState.SuccessBaby(it.myBaby)
            _growthAlbumState.value = GrowthAlbumState.SuccessBaby(it.others)
        }
    }

    fun changeBaby() = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.ChangeBaby
    }

    fun pickDate() {
        _growthAlbumState.value = GrowthAlbumState.PickDate
    }

}