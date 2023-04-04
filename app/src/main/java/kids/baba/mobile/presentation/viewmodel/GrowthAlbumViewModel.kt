package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.Article
import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.domain.model.CommentInput
import kids.baba.mobile.domain.usecase.*
import kids.baba.mobile.presentation.state.AlbumDetailUiState
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrowthAlbumViewModel @Inject constructor(
    private val getAlbumsFromBabyIdUseCase: GetAlbumsFromBabyIdUseCase,
    private val getOneBabyUseCase: GetOneBabyUseCase,
    private val postOneArticleUseCase: PostOneArticleUseCase,
    private val likeAlbumUseCase: LikeAlbumUseCase
) : ViewModel() {
    private val _growthAlbumState =
        MutableStateFlow<GrowthAlbumState>(GrowthAlbumState.UnInitialized)
    val growthAlbumState = _growthAlbumState

    var date = MutableStateFlow("")
    val baby = MutableStateFlow<Baby?>(null)
    val album = MutableStateFlow<Album?>(null)
    val albums = MutableStateFlow<List<Album>>(listOf())
    val width = MutableStateFlow(0)

    init {
        loadBaby()
    }

    fun loadAlbum(id: String, year: Int, month: Int) = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        getAlbumsFromBabyIdUseCase.getOneAlbum(id, year, month).catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect {
            Log.e("123", "${it}")
            _growthAlbumState.value = GrowthAlbumState.SuccessAlbum(it.album)
        }
    }

    fun pickDate() = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.PickDate
    }

    fun changeBaby() = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.ChangeBaby
    }

    private fun loadBaby() = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        getOneBabyUseCase.getOneBaby().catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect {
            _growthAlbumState.value = GrowthAlbumState.SuccessBaby(it)
        }
    }

    fun like() = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        likeAlbumUseCase.like(
            baby.value!!.babyId,
            album.value!!.contentId.toString()
        ).catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect {
            _growthAlbumState.value = GrowthAlbumState.Like(it.isLiked)
        }
    }

    fun postArticle(id: String, article: Article) = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        postOneArticleUseCase.post(id, article)
        _growthAlbumState.value = GrowthAlbumState.Post

    }
}