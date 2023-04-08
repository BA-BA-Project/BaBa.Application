package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.GetAlbumsFromBabyIdUseCase
import kids.baba.mobile.domain.usecase.GetBabiesUseCase
import kids.baba.mobile.domain.usecase.LikeAlbumUseCase
import kids.baba.mobile.domain.usecase.PostOneArticleUseCase
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GrowthAlbumViewModel @Inject constructor(
    private val getAlbumsFromBabyIdUseCase: GetAlbumsFromBabyIdUseCase,
    private val getBabiesUseCase: GetBabiesUseCase,
    private val postOneArticleUseCase: PostOneArticleUseCase,
    private val likeAlbumUseCase: LikeAlbumUseCase
) : ViewModel() {
    private val _growthAlbumState =
        MutableStateFlow<GrowthAlbumState>(GrowthAlbumState.Loading)
    val growthAlbumState = _growthAlbumState

    private val _selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _growthAlbumList = MutableStateFlow<List<AlbumUiModel>>(emptyList())
    val growthAlbumList = _growthAlbumList.asStateFlow()

    private var growthAlbumHash = HashMap<LocalDate, AlbumUiModel>()

    private val _selectedAlbum = MutableStateFlow<AlbumUiModel?>(AlbumUiModel(date = LocalDate.now()))
    val selectedAlbum = _selectedAlbum.asStateFlow()

    private val _selectedBaby = MutableStateFlow<BabyUiModel?>(null)
    val selectedBaby = _selectedBaby.asStateFlow()

    private val tempDate = LocalDate.now()
    private val tempAlbumList = List(5) { idx ->
        if (idx % 3 == 0) {
            null
        } else {
            AlbumUiModel(
                contentId = idx,
                name = "이호성$idx",
                relation = "아빠$idx",
                date = tempDate.minusDays(idx.toLong()),
                title = "제목$idx",
                like = idx % 2 == 0,
                photo = "https://www.shutterstock.com/image-photo/cute-little-african-american-infant-600w-1937038210.jpg",
                cardStyle = "TEST"
            )
        }
    }

    init {
        loadBaby()
        loadAlbum()
        selectDate(LocalDate.now())
    }

    private fun loadAlbum() = viewModelScope.launch {
        tempAlbumList.filterNotNull().forEach {
            growthAlbumHash[it.date] = it
        }
        growthAlbumHash[LocalDate.now()] = AlbumUiModel(date = LocalDate.now())
        _growthAlbumList.value = growthAlbumHash.values.sortedBy { it.date }

//        getOneAlbumUseCase.getOneAlbum(id).catch {
//            _growthAlbumState.value = GrowthAlbumState.Error(it)
//        }.collect {
//            _growthAlbumState.value = GrowthAlbumState.SuccessAlbum(it.album)
//        }
    }

    fun getAlbumIndex(): Int {
        return _growthAlbumList.value.indexOf(_selectedAlbum.value)
    }
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
//        if (growthAlbumHash[date] == null) {
//            val tempAlbum = AlbumUiModel(date = date)
//            val beforeDateAlbum = _growthAlbumList.value.filter { it.date.isBefore(date) }
//            val afterDateAlbum = _growthAlbumList.value.filter { it.date.isAfter(date) }
//            _growthAlbumList.value = beforeDateAlbum + tempAlbum + afterDateAlbum
//        } else {
//            _selectedAlbum.value = growthAlbumHash[date]
//            _growthAlbumList.value = growthAlbumHash.values.sortedBy { it.date }
//        }
    }

    fun getDateFromPosition(position: Int): LocalDate{
        val a = _growthAlbumList.value
        return _growthAlbumList.value[position].date
    }

    fun selectBaby(baby: BabyUiModel){
        _selectedBaby.value = baby
    }

    fun selectAlbum() {
        val date = _selectedDate.value
        var album = growthAlbumHash[date]

        if(album == null){
            album = AlbumUiModel(date = date)
            val beforeDateAlbum = _growthAlbumList.value.filter { it.date.isBefore(date) }
            val afterDateAlbum = _growthAlbumList.value.filter { it.date.isAfter(date) }
            _growthAlbumList.value = beforeDateAlbum + album + afterDateAlbum
        } else {
            _growthAlbumList.value = growthAlbumHash.values.sortedBy { it.date }
        }
        _selectedAlbum.value = album
    }

    private fun loadBaby() = viewModelScope.launch {
//        _growthAlbumState.value = GrowthAlbumState.Loading
//        getOneBabyUseCase.getOneBaby().catch {
//            _growthAlbumState.value = GrowthAlbumState.Error(it)
//        }.collect {
//            _growthAlbumState.value = GrowthAlbumState.SuccessBaby(it.myBaby)
//            _growthAlbumState.value = GrowthAlbumState.SuccessBaby(it.others)
//        }
        _selectedBaby.value = BabyUiModel("0","#FF1234","앙쥬0", true)
    }

    fun changeBaby() = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.ChangeBaby
    }

    fun pickDate() {
        _growthAlbumState.value = GrowthAlbumState.PickDate
    }
}