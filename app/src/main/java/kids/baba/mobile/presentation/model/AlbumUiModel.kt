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
    val cardStyle: CardStyleUiModel = CardStyleUiModel.CARD_BASIC_1,
    val isMyBaby: Boolean = false
) : Parcelable