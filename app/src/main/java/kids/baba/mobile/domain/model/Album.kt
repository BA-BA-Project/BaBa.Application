package kids.baba.mobile.domain.model

data class Album(
    val contentId: Int = -1,
    val name: String = "",
    val relation: String = "",
    val date: String = "",
    val title: String = "",
    val like: Boolean = false,
    val photo: String = "",
    val cardStyle: String = ""
)