package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.usecase.AddCommentUseCase
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
import kids.baba.mobile.presentation.view.AlbumDetailDialog.Companion.SELECTED_ALBUM_KEY
import kids.baba.mobile.presentation.view.BabyListBottomSheet.Companion.SELECTED_BABY_ID_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val getMemberUseCase: GetMemberUseCase,
    private val likeAlbumUseCase: LikeAlbumUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val getLikeDetailUseCase: GetLikeDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isPhotoExpended: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isPhotoExpended = _isPhotoExpended.asStateFlow()

    private val _eventFlow = MutableEventFlow<AlbumDetailEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private lateinit var member: MemberUiModel

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

    private val babyId = savedStateHandle[SELECTED_BABY_ID_KEY] ?: ""

    init {
        viewModelScope.launch {
            member = getMemberUseCase.getMe()
        }
        getAlbumDetailData()
    }

    private fun getAlbumDetailData() = viewModelScope.launch {
        val contentId = albumDetailUiState.value.albumDetail.album.contentId
        if (babyId.isEmpty()) {
            _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.album_not_found_error))
        } else {
            if (contentId == null) {
                _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.album_not_found_error))
            } else {
                runCatching {
                    val likeDetail = getLikeDetail(babyId, contentId)
                    val comments = getComments(babyId, contentId)
                    _albumDetailUiState.update { uiState ->
                        uiState.copy(
                            albumDetail = uiState.albumDetail.copy(
                                likeDetail = likeDetail,
                                likeCount = likeDetail.likeUsers.size,
                                comments = comments
                            )
                        )
                    }
                }.onFailure {
                    _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.album_not_found_error))
                }
            }
        }
    }

    fun addComment() = viewModelScope.launch {
//        val contentId = albumDetailUiState.value.albumDetail.album.contentId
//
//        val commentInput = CommentInput(tag = "", comment = comment.value)
////            _albumDetailUiState.value = AlbumDetailUiState.Loading
//            addCommentUseCase.add(babyId, contentId, commentInput)
////            _albumDetailUiState.value = AlbumDetailUiState.AddComment
//            comment.value = ""
    }

    private suspend fun getComments(babyId: String, contentId: String) =
        getCommentsUseCase.get(babyId, contentId).first().comments.map { it.toPresentation() }

    private suspend fun getLikeDetail(babyId: String, contentId: String) =
        getLikeDetailUseCase.get(babyId, contentId).first().toPresentation()

    fun setExpended(expended: Boolean) {
        if (expended != _isPhotoExpended.value) {
            _isPhotoExpended.value = expended
        }
    }

    fun setTag(memberName: String, memberId: String) {
        _commentTag.value = CommentTag(memberName,memberId)
    }

    fun deleteComment(comment: CommentUiModel) = viewModelScope.launch {

    }

    fun checkMyComment(comment: CommentUiModel) = member.memberId == comment.memberId
}