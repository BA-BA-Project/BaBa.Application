package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class AlbumDetailEvent{
    data class ShowSnackBar(@StringRes val message: Int): AlbumDetailEvent()
    object ShowAlbumConfig: AlbumDetailEvent()

    object DismissAlbumDetail: AlbumDetailEvent()
}
