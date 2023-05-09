package kids.baba.mobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemberUiModel(
    val memberId: String,
    val name: String,
    val introduction: String,
    val userIconUiModel: UserIconUiModel
) : Parcelable
