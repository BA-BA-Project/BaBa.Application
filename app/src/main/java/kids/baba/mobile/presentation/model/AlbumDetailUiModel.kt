package kids.baba.mobile.presentation.model


data class AlbumDetailUiModel(
    val likeCount: Int,
    val likeUsers: List<UserIconUiModel>,
    val commentCount: Int,
    val comments: List<CommentUiModel>?
)
