package kids.baba.mobile.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaData(
    val mediaName: String = "",
    val mediaPath: String = "",
    val mediaDate: String = ""
) : Parcelable