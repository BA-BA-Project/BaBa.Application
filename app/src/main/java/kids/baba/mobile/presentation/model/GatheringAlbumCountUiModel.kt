package kids.baba.mobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GatheringAlbumCountUiModel(
    val representativeAlbumUiModel: RepresentativeAlbumUiModel,
    val albumCounts: Int? = null
) : Parcelable