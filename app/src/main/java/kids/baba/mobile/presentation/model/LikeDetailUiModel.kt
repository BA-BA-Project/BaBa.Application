package kids.baba.mobile.presentation.model

data class LikeDetailUiModel(
    val likeUsersPreview: List<UserIconUiModel> = emptyList(),
    val likeUsers: List<UserUiModel> = emptyList()
)
