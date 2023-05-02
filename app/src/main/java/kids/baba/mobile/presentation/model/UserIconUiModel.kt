package kids.baba.mobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserIconUiModel(
    val userProfileIconUiModel: UserProfileIconUiModel,
    val iconColor: String
) : Parcelable