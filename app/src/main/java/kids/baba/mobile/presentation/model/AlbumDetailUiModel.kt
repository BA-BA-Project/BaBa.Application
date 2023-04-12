package kids.baba.mobile.presentation.model


data class AlbumDetailUiModel(
    val album: AlbumUiModel = AlbumUiModel(),
    val likeCount: Int = 0,
    val likeDetail: LikeDetailUiModel = LikeDetailUiModel(),
    val commentCount: Int = 0,
    val comments: List<CommentUiModel> = emptyList()
)
