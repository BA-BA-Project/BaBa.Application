package kids.baba.mobile.presentation.model


data class AlbumDetailUiModel(
    val album : AlbumUiModel = AlbumUiModel(),
    val likeUsers: List<UserIconUiModel>,
    val commentCount: Int,
    val comments: List<CommentUiModel>?
)
