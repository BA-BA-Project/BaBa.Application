package kids.baba.mobile.presentation.model

data class EditMemberUiModel(
    val nameTitle: String = "이름",
    val deleteTitle: String = "멤버 삭제하기",
    val delete: String = "삭제",
    var member: MemberUiModel? = null,
    var relation: String? = null
)