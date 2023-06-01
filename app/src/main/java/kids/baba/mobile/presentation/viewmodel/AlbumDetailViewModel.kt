package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.CommentInput
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.AddCommentUseCase
import kids.baba.mobile.domain.usecase.DeleteCommentUseCase
import kids.baba.mobile.domain.usecase.GetCommentsUseCase
import kids.baba.mobile.domain.usecase.GetLikeDetailUseCase
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.LikeAlbumUseCase
import kids.baba.mobile.presentation.event.AlbumDetailEvent
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.model.AlbumDetailUiModel
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.CommentTag
import kids.baba.mobile.presentation.model.CommentUiModel
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.state.AlbumDetailUiState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.dialog.AlbumDetailDialog.Companion.SELECTED_ALBUM_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val getMemberUseCase: GetMemberUseCase,
    private val likeAlbumUseCase: LikeAlbumUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val getLikeDetailUseCase: GetLikeDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isPhotoExpended: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isPhotoExpended = _isPhotoExpended.asStateFlow()

    private val _eventFlow = MutableEventFlow<AlbumDetailEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _member: MutableStateFlow<MemberUiModel?> = MutableStateFlow(null)
    val member = _member.asStateFlow()

    private val _albumDetailUiState = MutableStateFlow(
        AlbumDetailUiState(
            AlbumDetailUiModel(
                album = savedStateHandle[SELECTED_ALBUM_KEY] ?: AlbumUiModel()
            )
        )
    )
    val albumDetailUiState = _albumDetailUiState.asStateFlow()

    private val _commentTag: MutableStateFlow<CommentTag?> = MutableStateFlow(null)
    val commentTag = _commentTag.asStateFlow()

    val comment = MutableStateFlow("")

    val baby = EncryptedPrefs.getBaby(PrefsKey.BABY_KEY)
    val albumDateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd")
    init {
        initModel()
        getAlbumDetailData()
    }

    private fun initModel() {
        viewModelScope.launch {
            when(val result = getMemberUseCase.getMe()){
                is Result.Success -> _member.value = result.data.toPresentation()
                is Result.NetworkError -> _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_get_member_failed))
            }
        }
    }

    private fun getAlbumDetailData() = viewModelScope.launch {
        val contentId = albumDetailUiState.value.albumDetail.album.contentId
        if (contentId == null) {
            _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.album_not_found_error))
        } else {
            val likeDetailResult = getLikeDetailUseCase(baby.babyId, contentId)
            val commentsResult = getCommentsUseCase(baby.babyId, contentId)
            when {
                likeDetailResult is Result.Success && commentsResult is Result.Success -> {
                    val likeDetail = likeDetailResult.data
                    val comments = commentsResult.data
                    _albumDetailUiState.update { uiState ->
                        uiState.copy(
                            albumDetail = uiState.albumDetail.copy(
                                likeDetail = likeDetail.toPresentation(),
                                likeCount = likeDetail.likeUsers.size,
                                comments = comments.map { it.toPresentation() }
                            )
                        )
                    }
                }

                likeDetailResult is Result.NetworkError || commentsResult is Result.NetworkError -> {
                    _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_network_failed))
                }

                else -> _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.album_not_found_error))
            }

        }

    }

    fun addComment(addSuccess: () -> Unit) = viewModelScope.launch {
        val contentId = albumDetailUiState.value.albumDetail.album.contentId

        if (contentId != null) {
            val commentInput =
                CommentInput(tag = commentTag.value?.memberId ?: "", comment = comment.value)
            when (addCommentUseCase(baby.babyId, contentId, commentInput)) {
                is Result.Success -> {
                    getAlbumDetailData()
                    comment.value = ""
                    addSuccess()
                }

                is Result.NetworkError -> _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_add_comment_failed))
            }
        }

    }

    fun setExpended(expended: Boolean) {
        if (expended != _isPhotoExpended.value) {
            _isPhotoExpended.value = expended
        }
    }

    fun setTag(memberName: String, memberId: String) {
        _commentTag.value = CommentTag(memberName, memberId)
    }

    fun deleteComment(commentId: String) = viewModelScope.launch {
        val contentId = albumDetailUiState.value.albumDetail.album.contentId
        if (contentId != null) {
            when (deleteCommentUseCase(baby.babyId, contentId, commentId)) {
                is Result.Success -> getAlbumDetailData()
                is Result.NetworkError -> _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_delete_comment_failed))
            }
        }
    }

    fun likeAlbum() = viewModelScope.launch {
        val contentId = albumDetailUiState.value.albumDetail.album.contentId
        if (contentId != null) {
            when (val result = likeAlbumUseCase(baby.babyId, contentId)) {
                is Result.Success -> {
                    getAlbumDetailData()
                    _albumDetailUiState.update { uiState ->
                        uiState.copy(
                            albumDetail = uiState.albumDetail.copy(
                                album = uiState.albumDetail.album.copy(like = result.data)
                            )
                        )
                    }
                }

                is Result.NetworkError -> _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.baba_like_album_failed))
            }
        }
    }

    fun checkMyComment(comment: CommentUiModel) = member.value?.memberId == comment.memberId

    fun showAlbumConfig() = viewModelScope.launch {
        _eventFlow.emit(AlbumDetailEvent.ShowAlbumConfig)
    }

    fun dismissDialog() = viewModelScope.launch {
        _eventFlow.emit(AlbumDetailEvent.DismissAlbumDetail)
    }
}