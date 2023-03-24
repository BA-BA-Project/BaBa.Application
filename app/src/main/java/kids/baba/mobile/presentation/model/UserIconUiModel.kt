package kids.baba.mobile.presentation.model

import androidx.annotation.DrawableRes

data class UserIconUiModel(
    @DrawableRes
    val iconName: Int,
    val iconColor: String
)