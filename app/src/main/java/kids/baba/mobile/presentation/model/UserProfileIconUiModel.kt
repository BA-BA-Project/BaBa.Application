package kids.baba.mobile.presentation.model

import androidx.annotation.DrawableRes
import kids.baba.mobile.R

enum class UserProfileIconUiModel(
    @DrawableRes
    iconRes: Int,
    selected: Boolean
) {
    PROFILE_W_1(R.drawable.profile_w_1, false),
    PROFILE_W_2(R.drawable.profile_w_2, false),
    PROFILE_W_4(R.drawable.profile_w_4, false),
    PROFILE_W_3(R.drawable.profile_w_3, false),
    PROFILE_W_5(R.drawable.profile_w_5, false),
    PROFILE_M_1(R.drawable.profile_m_1, false),
    PROFILE_M_2(R.drawable.profile_m_2, false),
    PROFILE_M_3(R.drawable.profile_m_3, false),
    PROFILE_M_4(R.drawable.profile_m_4, false),
    PROFILE_M_5(R.drawable.profile_m_5, false),
    PROFILE_M_6(R.drawable.profile_m_6, false),
    PROFILE_G_1(R.drawable.profile_g_1, false),
    PROFILE_G_2(R.drawable.profile_g_2, false),
    PROFILE_G_3(R.drawable.profile_g_3, false),
    PROFILE_G_4(R.drawable.profile_g_4, false),
    PROFILE_BABY_1(R.drawable.profile_baby_1,false)
}