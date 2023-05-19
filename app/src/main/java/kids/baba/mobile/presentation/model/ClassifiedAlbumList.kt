package kids.baba.mobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClassifiedAlbumList(
    val classifiedAlbumList: List<AlbumUiModel>
): Parcelable
