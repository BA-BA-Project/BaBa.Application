package kids.baba.mobile.presentation.model

data class EditGroupBottomSheetUiModel(
    val familyName: String = "가족 이름",
    val familyButton: String = "편집",
    val permission: String = "성장앨범 업로드 권한",
    val groupColor: String = "그룹 컬러",
    var permissionDesc: String = "없음",
    val deleteTitle: String = "그룹 삭제",
    val deleteDesc: String = "삭제하기",
    val addMemberTitle: String = "멤버 초대"
)
