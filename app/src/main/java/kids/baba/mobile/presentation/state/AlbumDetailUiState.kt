package kids.baba.mobile.presentation.state

import kids.baba.mobile.presentation.model.AlbumDetailUiModel

data class AlbumDetailUiState(
    val albumDetail: AlbumDetailUiModel = AlbumDetailUiModel(),
    val isPhotoExpended: Boolean = false
)