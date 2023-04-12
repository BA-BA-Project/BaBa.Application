package kids.baba.mobile.presentation.model


data class AlbumDetailUiModel(
    val album : AlbumUiModel = AlbumUiModel(),
    val likeUsers: List<UserIconUiModel> = emptyList(),
    val commentCount: Int = 0,
    val comments: List<CommentUiModel> = emptyList()
)
