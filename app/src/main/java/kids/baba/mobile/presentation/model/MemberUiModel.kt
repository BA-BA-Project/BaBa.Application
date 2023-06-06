package kids.baba.mobile.presentation.model

import kotlinx.parcelize.Parcelize

@Parcelize
data class MemberUiModel(
    val memberId: String,
    val name: String,
    val introduction: String, // 그룹 어댑터에서의 introduction 나와의 관계를 의미함
    val userIconUiModel: UserIconUiModel
) : GroupMember()
