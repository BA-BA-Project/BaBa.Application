package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.Member
import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel

fun MemberModel.toPresentation(): MemberUiModel {
    return MemberUiModel(
        memberId = memberId,
        name = name,
        introduction = introduction,
        userIconUiModel = UserIconUiModel(
            userProfileIconUiModel = UserProfileIconUiModel.valueOf(iconName),
            iconColor = iconColor
        )
    )
}

fun MemberUiModel.toDomain(): MemberModel {
    return MemberModel(
        memberId = memberId,
        name = name,
        introduction = introduction,
        iconName = userIconUiModel.userProfileIconUiModel.name,
        iconColor = userIconUiModel.iconColor
    )
}

fun Member.toMemberUiModel(): MemberUiModel {
    return MemberUiModel(
        name = name,
        memberId = memberId,
        introduction = relationName,
        userIconUiModel = UserIconUiModel(iconColor = iconColor, userProfileIconUiModel = getUserProfileIconUiModel(iconName))
    )
}