package kids.baba.mobile.domain.model

data class Album(
    val contentId: Int,
    val name: String,
    val relation: String,
    val date: String,
    val title: String,
    val like: Boolean,
    val photo: String,
    val cardStyle: String
)