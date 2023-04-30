package kids.baba.mobile.presentation.state

import kids.baba.mobile.domain.model.Comment
import kids.baba.mobile.domain.model.LikeDetailResponse
import kids.baba.mobile.presentation.model.AlbumDetailUiModel
import kids.baba.mobile.presentation.model.AlbumUiModel

sealed class AlbumDetailUiState {
    object Loading : AlbumDetailUiState()

    object AddComment : AlbumDetailUiState()
    data class LoadComments(val comments: List<Comment>) : AlbumDetailUiState()

    data class GetLikeDetail(val data: LikeDetailResponse) : AlbumDetailUiState()
    data class Like(val data: Boolean) : AlbumDetailUiState()
    data class Success(val albumDetail: AlbumDetailUiModel, val album: AlbumUiModel) :
        AlbumDetailUiState()

    object Failure : AlbumDetailUiState()

    data class Error(val error: Throwable) : AlbumDetailUiState()
}