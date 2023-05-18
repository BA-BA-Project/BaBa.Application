package kids.baba.mobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class RepresentativeAlbumUiModel(
    val photo: String = "",
    val date: LocalDate
) : Parcelable