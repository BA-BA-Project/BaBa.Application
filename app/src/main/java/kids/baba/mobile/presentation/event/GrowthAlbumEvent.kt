package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class GrowthAlbumEvent{
    data class ShowSnackBar(@StringRes val message: Int): GrowthAlbumEvent()
}
