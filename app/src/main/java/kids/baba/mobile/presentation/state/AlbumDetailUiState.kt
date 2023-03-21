package kids.baba.mobile.presentation.state

import kids.baba.mobile.presentation.model.AlbumDetailUiModel

sealed class AlbumDetailUiState {
    object Loading : AlbumDetailUiState()
    data class Success(val data: AlbumDetailUiModel) : AlbumDetailUiState()
    object Failure : AlbumDetailUiState()
}