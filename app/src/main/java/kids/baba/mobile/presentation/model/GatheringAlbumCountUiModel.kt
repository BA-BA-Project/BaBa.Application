package kids.baba.mobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GatheringAlbumCountUiModel(
    val albumUiModel: AlbumUiModel,
    val albumCounts: Int? = null
) : Parcelable