package kids.baba.mobile.presentation.model

data class InviteMemberUiModel(
    val topAppBarTitle: String = "멤버 초대",
    val bannerTitle: String = "아이와 나의 소식을 공유할\n멤버를 초대해요",
    val bannerDesc: String = "직접 생성한 아이의 성장앨범과\n자신의 일기를 공유할 수 있어요.",
    val inputGroupTitle: String = "초대 멤버의 소속 그룹",
    val inputGroupDesc: String = "초대받을 멤버가 어떤 그룹인가요?",
    val inputRelationTitle: String = "초대 멤버와 아이의 관계",
    val inputRelationDesc: String = "초대받을 멤버의 별명으로 초대 멤버와\n그룹에게 공개됩니다.",
    val inputRelationButton: String = "편집"
)