package kids.baba.mobile.domain.model

import java.time.LocalDateTime

data class Comment(
    val commentId: String,
    val memberId: String,
    val name: String,
    val relation: String,
    val iconName: String,
    val iconColor: String,
    val tag: String,
    val comment: String,
    val createAt: LocalDateTime
)