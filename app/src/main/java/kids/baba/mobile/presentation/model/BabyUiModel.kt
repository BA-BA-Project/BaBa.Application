package kids.baba.mobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BabyUiModel(
    val babyId : String,
    val groupColor: String,
    val name: String,
    var selected: Boolean = false
): Parcelable