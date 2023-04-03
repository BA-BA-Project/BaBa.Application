package kids.baba.mobile.domain.model

import kids.baba.mobile.presentation.model.CommentUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel
import java.time.LocalDateTime

data class Comment(
    val commentId: Int,
    val memberId: String,
    val name: String,
    val relation: String,
    val iconName: String,
    val iconColor: String,
    val tag: String,
    val comment: String,
    val createdAt: LocalDateTime
) {
    fun toCommentUiModel() = CommentUiModel(
        commentId = commentId,
        memberId = memberId,
        name = name,
        relation = relation,
        iconColor = iconColor,
        tag = tag,
        comment = comment,
        createdAt = createdAt,
        profileIcon = UserProfileIconUiModel.PROFILE_G_1
    )
}