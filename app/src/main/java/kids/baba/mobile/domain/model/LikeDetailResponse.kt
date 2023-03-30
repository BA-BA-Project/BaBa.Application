package kids.baba.mobile.domain.model

data class LikeDetailResponse(
    val likeUsersPreview: List<Icon>,
    val likeUsers: List<User>
)
