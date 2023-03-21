package kids.baba.mobile.domain.model

data class AlbumDetailResponse(
    val likeCount: Int,
    val likeUsers: List<UserIcon>,
    val commentCount: Int,
    val comments: List<Comment>
)



