package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.User
import kids.baba.mobile.domain.model.UserIcon
import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel
import kids.baba.mobile.presentation.model.UserUiModel

fun User.toPresentation() =
    UserUiModel(
        userIconUiModel = UserIconUiModel(
            iconColor = iconColor,
            userProfileIconUiModel = UserProfileIconUiModel.valueOf(iconName)
        ),
        userName = name
    )

fun UserIcon.toPresentation() = UserIconUiModel(
    iconColor = iconColor,
    userProfileIconUiModel = UserProfileIconUiModel.valueOf(iconName)
)