package kids.baba.mobile.presentation.model


data class AlbumDetailUiModel(
    val album : AlbumUiModel = AlbumUiModel(),
    val likeDetail: LikeDetailUiModel = LikeDetailUiModel(),
    val commentCount: Int = 0,
    val comments: List<CommentUiModel> = emptyList()
)
