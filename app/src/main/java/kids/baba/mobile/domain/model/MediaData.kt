package kids.baba.mobile.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaData(
    val mediaName: String = "",
    val mediaUri: String = "",
    val mediaDate: String = ""
) : Parcelable