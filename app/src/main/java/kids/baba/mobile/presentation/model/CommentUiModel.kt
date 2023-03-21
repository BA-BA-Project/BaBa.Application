package kids.baba.mobile.presentation.model

import androidx.annotation.DrawableRes
import java.time.LocalDateTime

data class CommentUiModel(
    val commentId: Int,
    val memberId: String,
    val name: String,
    val relation: String,
    @DrawableRes
    val iconName: Int,
    val iconColor: String,
    val tag: String,
    val comment: String,
    val createdAt: LocalDateTime
)