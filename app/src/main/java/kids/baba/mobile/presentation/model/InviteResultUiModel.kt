package kids.baba.mobile.presentation.model

data class InviteResultUiModel(
    val addBabyTitle: String = "아이 추가하기",
    val addBabyBannerTitle: String = "아이가 추가되었어요!",
    val addBabyBannerDesc: String = "아이의 소식을 받고 소통해봐요 :)",
    val addBabyNameTitle: String = "아이 이름",
    var addBabyNameDesc: String = "티티",
    val addBabyGroupTitle: String = "나의 소속 그룹",
    var addBabyGroupDesc: String = "친구",
    val addBabyRelationTitle: String = "나와 아이와 관계",
    var addBabyRelationDesc: String = "이모",
    val addBabyPermissionTitle: String = "성장앨범 업로드 권한",
    var addBabyPermissionDesc: String = "없음"
)