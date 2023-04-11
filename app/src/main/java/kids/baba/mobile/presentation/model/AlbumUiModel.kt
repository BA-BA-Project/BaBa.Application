package kids.baba.mobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class AlbumUiModel (
    val contentId: Int? = null,
    val name: String = "",
    val relation: String = "",
    val date: LocalDate,
    val title: String = "",
    val like: Boolean = false,
    val photo: String = "",
    val cardStyle: String = ""
) : Parcelable


//val contentId: Int? = null,
//val name: String ? = null,
//val relation: String? = null,
//val date: LocalDate? = null,
//val title: String? = null,
//val like: Boolean? = null,
//val photo: String? = null,
//val cardStyle: String? = null