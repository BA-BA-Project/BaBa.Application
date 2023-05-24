package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class AlbumConfigEvent{
    object DialogDismiss: AlbumConfigEvent()
    data class ShowSnackBar(@StringRes val message: Int): AlbumConfigEvent()
}
