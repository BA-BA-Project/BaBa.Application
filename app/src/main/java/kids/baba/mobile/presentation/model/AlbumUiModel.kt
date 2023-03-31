package kids.baba.mobile.presentation.model

data class AlbumUiModel (
    val contentId: Int,
    val name: String,
    val relation: String,
    val date: String,
    val title: String,
    val like: Boolean,
    val photo: String,
    val cardStyle: String
)