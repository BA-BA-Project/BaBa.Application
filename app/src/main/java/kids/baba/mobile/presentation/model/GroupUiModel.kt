package kids.baba.mobile.presentation.model

import kids.baba.mobile.domain.model.Member

data class GroupUiModel(
    val ownerFamily: String,
    val groupName: String,
    val family: Boolean,
    val members: List<Member>
)