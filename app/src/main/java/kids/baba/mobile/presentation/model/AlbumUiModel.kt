package kids.baba.mobile.presentation.model

import java.time.LocalDate

data class AlbumUiModel (
    val contentId: Int,
    val name: String,
    val relation: String,
    val date: LocalDate,
    val title: String,
    val like: Boolean,
    val photo: String,
    val cardStyle: String
)