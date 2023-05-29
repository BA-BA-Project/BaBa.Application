package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class GrowthAlbumEvent{
    data class ShowSnackBar(@StringRes val message: Int): GrowthAlbumEvent()
    object ShowBabyList: GrowthAlbumEvent()
    object ShowAlbumConfig: GrowthAlbumEvent()
    object ShowAlbumDetail: GrowthAlbumEvent()
}
