package kids.baba.mobile.presentation.model

data class AddGroupUiModel(
    val title: String = "그룹 만들기",
    val bannerTitle: String = "그룹을 만들어 소통을 관리해요!",
    val bannerDescription: String = "초대된 멤버는 자신의 그룹과\n가족 그룹의 댓글만 확인할 수 있어요",
    val nameViewTitle: String = "그룹 이름",
    val nameViewDesc: String = "어떤 그룹을 만들 건가요?",
    val colorViewTitle: String = "그룹 컬러",
    val permissionViewTitle: String = "성장앨범 업로드 권한",
    val permissionViewDesc: String = "없음"
)