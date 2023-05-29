package kids.baba.mobile.domain.model

data class LikeDetailResponse(
    val likeUsersPreview: List<UserIcon>,
    val likeUsers: List<User>
)