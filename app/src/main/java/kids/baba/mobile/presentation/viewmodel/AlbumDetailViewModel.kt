package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.core.error.TokenEmptyException
import kids.baba.mobile.domain.model.Comment
import kids.baba.mobile.domain.model.CommentInput
import kids.baba.mobile.domain.model.LikeDetailResponse
import kids.baba.mobile.domain.usecase.*
import kids.baba.mobile.presentation.event.AlbumDetailEvent
import kids.baba.mobile.presentation.model.AlbumDetailUiModel
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.CommentUiModel
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel
import kids.baba.mobile.presentation.state.AlbumDetailUiState
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val getMemberUseCase: GetMemberUseCase,
    private val likeAlbumUseCase: LikeAlbumUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val getLikeDetailUseCase: GetLikeDetailUseCase
) : ViewModel() {

    val albumDetail = MutableStateFlow<AlbumDetailUiModel?>(null)
    val album = MutableStateFlow<AlbumUiModel?>(null)

    private val _isPhotoExpended: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isPhotoExpended = _isPhotoExpended.asStateFlow()

    private val _eventFlow = MutableEventFlow<AlbumDetailEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _member = MutableStateFlow<MemberUiModel?>(null)
    val member = _member.asStateFlow()

    private val _albumDetailUiState =
        MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val albumDetailUiState = _albumDetailUiState

    val comment = MutableStateFlow<String?>(null)
    val comments = MutableStateFlow<List<Comment>?>(null)
    val likeDetail = MutableStateFlow<LikeDetailResponse?>(null)

    init {
        initModel()
    }

    private fun initModel() {
        viewModelScope.launch {
            runCatching { getMemberUseCase.getMe() }.onSuccess {
                _member.value = it
            }.onFailure {
                if (it is TokenEmptyException) {
                    _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_token_empty_error))
                } else {
                    _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_unknown_error))
                }
            }
        }
    }

    fun like() = viewModelScope.launch {
        _albumDetailUiState.value = AlbumDetailUiState.Loading
        likeAlbumUseCase.like(
            "4972ef56-a7f0-4c9f-a1ab-798ac04fb7fb",
            album.value!!.contentId.toString()
        ).catch {
            _albumDetailUiState.value = AlbumDetailUiState.Error(it)
        }.collect {
            _albumDetailUiState.value = AlbumDetailUiState.Like(it.isLiked)
        }
    }

    fun addComment(comment:String) =
        viewModelScope.launch {
            val id = "4972ef56-a7f0-4c9f-a1ab-798ac04fb7fb"
            val contentId = album.value!!.contentId.toString()
            val commentInput = CommentInput(tag = "", comment = comment)
            _albumDetailUiState.value = AlbumDetailUiState.Loading
            addCommentUseCase.add(id, contentId, commentInput)
            _albumDetailUiState.value = AlbumDetailUiState.AddComment
        }

    fun getComments() = viewModelScope.launch {
        _albumDetailUiState.value = AlbumDetailUiState.Loading
        getCommentsUseCase.get(album.value!!.contentId.toString()).catch {
            _albumDetailUiState.value = AlbumDetailUiState.Error(it)
        }.collect {
            _albumDetailUiState.value = AlbumDetailUiState.LoadComments(it.comments)
        }
    }

    fun getLikeDetail() = viewModelScope.launch {
        _albumDetailUiState.value = AlbumDetailUiState.Loading
        val id = "4972ef56-a7f0-4c9f-a1ab-798ac04fb7fb"
        getLikeDetailUseCase.get(id = id, contentId = album.value!!.contentId.toString()).catch {
            _albumDetailUiState.value = AlbumDetailUiState.Error(it)
        }.collect {
            _albumDetailUiState.value = AlbumDetailUiState.GetLikeDetail(it)
        }
    }

    fun setExpended(expended: Boolean) {
        if (expended != _isPhotoExpended.value) {
            _isPhotoExpended.value = expended
        }
    }

}