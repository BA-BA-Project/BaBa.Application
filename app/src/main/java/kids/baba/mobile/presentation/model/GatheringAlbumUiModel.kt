package kids.baba.mobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class GatheringMonthAlbumUiModel(
    val contentId: Int? = null,
    val name: String = "",
    val relation: String = "",
    val date: LocalDate,
    val title: String = "",
    val like: Boolean = false,
    val photo: String = "",
    val cardStyle: String = "",
    val isMyBaby: Boolean = false
) : Parcelable

@Parcelize
data class GatheringYearAlbumUiModel(
    val contentId: Int? = null,
    val name: String = "",
    val relation: String = "",
    val date: LocalDate,
    val title: String = "",
    val like: Boolean = false,
    val photo: String = "",
    val cardStyle: String = "",
    val isMyBaby: Boolean = false
) : Parcelable

@Parcelize
data class GatheringAllAlbumUiModel(
    val contentId: Int? = null,
    val name: String = "",
    val relation: String = "",
    val date: LocalDate,
    val title: String = "",
    val like: Boolean = false,
    val photo: String = "",
    val cardStyle: String = "",
    val isMyBaby: Boolean = false
) : Parcelable