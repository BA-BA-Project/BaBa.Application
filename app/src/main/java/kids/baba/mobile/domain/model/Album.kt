package kids.baba.mobile.domain.model

import java.time.LocalDate

data class Album(
    val contentId: String? = null,
    val ownerName: String = "",
    val relation: String = "",
    val date: LocalDate,
    val title: String = "",
    val like: Boolean = false,
    val photo: String = "",
    val cardStyle: String = ""
)