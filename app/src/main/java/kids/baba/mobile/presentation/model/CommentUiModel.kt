package kids.baba.mobile.presentation.model

import java.time.LocalDateTime

data class CommentUiModel(
    val commentId: String,
    val memberId: String,
    val name: String,
    val relation: String,
    val profileIcon: UserProfileIconUiModel,
    val iconColor: String,
    val tag: String,
    val comment: String,
    val createdAt: LocalDateTime?
)