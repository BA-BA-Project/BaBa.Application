package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.UserProfileIcon
import kids.baba.mobile.presentation.model.UserProfileIconUiModel

fun UserProfileIconUiModel.toDomain(): UserProfileIcon {
    return when (this) {
        UserProfileIconUiModel.PROFILE_G_1 -> UserProfileIcon.PROFILE_G_1
        UserProfileIconUiModel.PROFILE_W_1 -> UserProfileIcon.PROFILE_W_1
        UserProfileIconUiModel.PROFILE_W_2 -> UserProfileIcon.PROFILE_W_2
        UserProfileIconUiModel.PROFILE_W_3 -> UserProfileIcon.PROFILE_W_3
        UserProfileIconUiModel.PROFILE_W_4 -> UserProfileIcon.PROFILE_W_4
        UserProfileIconUiModel.PROFILE_W_5 -> UserProfileIcon.PROFILE_W_5
        UserProfileIconUiModel.PROFILE_M_1 -> UserProfileIcon.PROFILE_M_1
        UserProfileIconUiModel.PROFILE_M_2 -> UserProfileIcon.PROFILE_M_2
        UserProfileIconUiModel.PROFILE_M_3 -> UserProfileIcon.PROFILE_M_3
        UserProfileIconUiModel.PROFILE_M_4 -> UserProfileIcon.PROFILE_M_4
        UserProfileIconUiModel.PROFILE_M_5 -> UserProfileIcon.PROFILE_M_5
        UserProfileIconUiModel.PROFILE_M_6 -> UserProfileIcon.PROFILE_M_6
        UserProfileIconUiModel.PROFILE_G_2 -> UserProfileIcon.PROFILE_G_2
        UserProfileIconUiModel.PROFILE_G_3 -> UserProfileIcon.PROFILE_G_3
        UserProfileIconUiModel.PROFILE_G_4 -> UserProfileIcon.PROFILE_G_4
        UserProfileIconUiModel.PROFILE_BABY_1 -> UserProfileIcon.PROFILE_BABY_1
    }
}