package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.Article
import kids.baba.mobile.domain.model.CommentInput
import kids.baba.mobile.domain.usecase.*
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
    private val likeAlbumUseCase: LikeAlbumUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val getLikeDetailUseCase: GetLikeDetailUseCase
) : ViewModel() {
    private val _growthAlbumState =
        MutableStateFlow<GrowthAlbumState>(GrowthAlbumState.UnInitialized)
    val growthAlbumState = _growthAlbumState.asStateFlow()

    fun loadAlbum(id: String, year: Int, month: Int) = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        getAlbumsFromBabyIdUseCase.getOneAlbum(id, year, month).catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect {
            Log.e("123", "${it}")
            _growthAlbumState.value = GrowthAlbumState.SuccessAlbum(it.album)
        }
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

    fun postArticle(id: String, article: Article) = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        postOneArticleUseCase.post(id, article)
        _growthAlbumState.value = GrowthAlbumState.Post

    }

    fun like(id: String, contentId: String) = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        likeAlbumUseCase.like(id, contentId).catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect {
            _growthAlbumState.value = GrowthAlbumState.Like(it.isLiked)
        }
    }

    fun addComment(id: String, contentId: String, commentInput: CommentInput) =
        viewModelScope.launch {
            _growthAlbumState.value = GrowthAlbumState.Loading
            addCommentUseCase.add(id, contentId, commentInput)
            _growthAlbumState.value = GrowthAlbumState.AddComment
        }

    fun getComments(contentId: String) = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        getCommentsUseCase.get(contentId).catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect{
            _growthAlbumState.value = GrowthAlbumState.LoadComments(it.comments)
        }
    }

    fun getLikeDetail(contentId: String) = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        getLikeDetailUseCase.get(contentId).catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect{
            _growthAlbumState.value = GrowthAlbumState.GetLikeDetail(it)
        }
    }
}