package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class PostAlbumEvent {
    data class ShowSnackBar(@StringRes val text: Int) : PostAlbumEvent()
    object MoveToMain: PostAlbumEvent()
}