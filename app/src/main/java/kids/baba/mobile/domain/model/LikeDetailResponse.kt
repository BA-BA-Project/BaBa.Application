package kids.baba.mobile.domain.model

import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel

data class LikeDetailResponse(
    val likeUsersPreview: List<Icon>,
    val likeUsers: List<User>
) {
    fun toIconUiModel() = likeUsersPreview.map {
        UserIconUiModel(
            iconColor = it.iconColor,
            userProfileIconUiModel = UserProfileIconUiModel.PROFILE_G_1
        )
    }
}
