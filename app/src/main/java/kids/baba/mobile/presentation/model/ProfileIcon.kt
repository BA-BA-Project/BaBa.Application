package kids.baba.mobile.presentation.model

import androidx.annotation.DrawableRes

data class ProfileIcon(
    val name: String,
    @DrawableRes
    val icon: Int,
    val selected: Boolean)

