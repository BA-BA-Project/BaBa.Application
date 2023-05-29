package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.LikeDetailResponse
import kids.baba.mobile.presentation.model.LikeDetailUiModel

fun LikeDetailResponse.toPresentation() =
    LikeDetailUiModel(
        likeUsersPreview = likeUsersPreview.map { it.toPresentation() },
        likeUsers = likeUsers.map { it.toPresentation() }
    )