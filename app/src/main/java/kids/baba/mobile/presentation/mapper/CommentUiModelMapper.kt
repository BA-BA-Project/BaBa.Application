package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.Comment
import kids.baba.mobile.presentation.model.CommentUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel

fun Comment.toPresentation() =
    CommentUiModel(
        commentId = commentId,
        memberId = memberId,
        name = name,
        relation = relation,
        iconColor = iconColor,
        tag = tag,
        comment = comment,
        createAt = createAt,
        profileIcon = UserProfileIconUiModel.valueOf(iconName)
    )