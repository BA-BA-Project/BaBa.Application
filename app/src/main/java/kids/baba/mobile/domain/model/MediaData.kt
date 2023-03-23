package kids.baba.mobile.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class MediaData (
    val mediaName: String = "",
    val mediaPath: String = "",
    val mediaDate: String = ""
): Parcelable