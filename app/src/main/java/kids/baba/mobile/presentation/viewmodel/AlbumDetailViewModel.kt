package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.domain.model.Comment
import kids.baba.mobile.domain.model.LikeDetailResponse
import kids.baba.mobile.domain.usecase.AddCommentUseCase
import kids.baba.mobile.domain.usecase.GetCommentsUseCase
import kids.baba.mobile.domain.usecase.GetLikeDetailUseCase
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.LikeAlbumUseCase
import kids.baba.mobile.presentation.event.AlbumDetailEvent
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.model.AlbumDetailUiModel
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.state.AlbumDetailUiState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.AlbumDetailDialog.Companion.SELECTED_ALBUM_KEY
import kids.baba.mobile.presentation.view.BabyListBottomSheet.Companion.SELECTED_BABY_ID_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //TODO 특정 날짜의 앨범만 호출하는 기능이 필요?

    val albumDetail = MutableStateFlow<AlbumDetailUiModel?>(null)
    val album = MutableStateFlow<AlbumUiModel?>(savedStateHandle[SELECTED_ALBUM_KEY])

    private val _isPhotoExpended: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isPhotoExpended = _isPhotoExpended.asStateFlow()

    private val _eventFlow = MutableEventFlow<AlbumDetailEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _member = MutableStateFlow<MemberUiModel?>(null)
    val member = _member.asStateFlow()

    private val _albumDetailUiState = MutableStateFlow(
        AlbumDetailUiState(
            AlbumDetailUiModel(
                album = savedStateHandle[SELECTED_ALBUM_KEY] ?: AlbumUiModel()
            )
        )
    )
    val albumDetailUiState = _albumDetailUiState.asStateFlow()

    private val comments = MutableStateFlow<List<Comment>?>(null)

    private val _baby = MutableStateFlow<Baby?>(null)
    val baby = _baby
    private val likeDetail = MutableStateFlow<LikeDetailResponse?>(null)

    val comment = MutableStateFlow("")

    init {

    }

    fun getAlbumDetailData() = viewModelScope.launch {
        val babyId = savedStateHandle[SELECTED_BABY_ID_KEY] ?: ""
        val contentId = albumDetailUiState.value.albumDetail.album.contentId
        if (babyId.isEmpty()) {
            _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.album_not_found_error))
        } else {
            if (contentId == null) {
                _eventFlow.emit(AlbumDetailEvent.ShowSnackBar(R.string.album_not_found_error))
            } else {
                runCatching {
                    getLikeDetail(babyId, contentId)
                    getComments(babyId, contentId)
                }
            }
        }
    }

    fun fetch() = viewModelScope.launch {
//        getComments().join()
//        getLikeDetail().join()
//        showComment()
    }

    fun like() = viewModelScope.launch {
//        val babyId = _baby.value?.babyId ?: return@launch
//        val contentId = album.value?.contentId
////        _albumDetailUiState.value = AlbumDetailUiState.Loading
//        likeAlbumUseCase.like(
//            babyId, contentId
//        ).catch {
////            _albumDetailUiState.value = AlbumDetailUiState.Error(it)
//        }.collect {
////            _albumDetailUiState.value = AlbumDetailUiState.Like(it.isLiked)
//        }
    }

    fun addComment() =
        viewModelScope.launch {
//            val babyId = _baby.value?.babyId ?: return@launch
//            val contentId = album.value?.contentId
//            val commentInput = CommentInput(tag = "", comment = comment.value)
////            _albumDetailUiState.value = AlbumDetailUiState.Loading
//            addCommentUseCase.add(babyId, contentId, commentInput)
////            _albumDetailUiState.value = AlbumDetailUiState.AddComment
//            comment.value = ""
        }

    private suspend fun getComments(babyId: String, contentId: String) =
        getCommentsUseCase.get(babyId, contentId).collect {
            _albumDetailUiState.update { state ->
                state.copy(
                    albumDetail = state.albumDetail.copy(
                        comments = it.comments.map { comment ->
                            comment.toPresentation()
                        },
                        commentCount = it.comments.size
                    )
                )
            }
        }

    private fun showComment() = viewModelScope.launch {
//        val tempAlbumDetail = AlbumDetailUiModel(
//            likeUsers = listOf(
//                UserIconUiModel(UserProfileIconUiModel.PROFILE_G_1, "#FFA500"),
//                UserIconUiModel(UserProfileIconUiModel.PROFILE_G_2, "#BACEE0"),
//                UserIconUiModel(UserProfileIconUiModel.PROFILE_G_3, "#629755")
//            ),
//            commentCount = comments.value?.size ?: 0,
////            comments = comments.value?.map { it.toCommentUiModel() }
//        )
//        albumDetail.value = tempAlbumDetail
    }

    private suspend fun getLikeDetail(babyId: String, contentId: String) =
        getLikeDetailUseCase.get(babyId, contentId).collect {
            _albumDetailUiState.update { state ->
                state.copy(
                    albumDetail = state.albumDetail.copy(
                        likeDetail = it.toPresentation(),
                        likeCount = it.likeUsers.size
                    )
                )
            }
        }

    fun setExpended(expended: Boolean) {
        if (expended != _isPhotoExpended.value) {
            _isPhotoExpended.value = expended
        }
    }
}