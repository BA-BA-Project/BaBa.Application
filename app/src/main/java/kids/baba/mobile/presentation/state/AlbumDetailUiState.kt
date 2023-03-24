package kids.baba.mobile.presentation.state

import kids.baba.mobile.presentation.model.AlbumDetailUiModel
import kids.baba.mobile.presentation.model.AlbumUiModel

sealed class AlbumDetailUiState {
    object Loading : AlbumDetailUiState()
    data class Success(val albumDetail: AlbumDetailUiModel, val album: AlbumUiModel ) : AlbumDetailUiState()
    object Failure : AlbumDetailUiState()
}