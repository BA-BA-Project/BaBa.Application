package kids.baba.mobile.presentation.model

data class AddBabyUiModel(
    val title: String = "아이 추가하기",
    val bannerTitle: String = "내 아이를 추가해봐요",
    val bannerDesc: String = "직접 성장앨범을 기록할 아이를 만들어요.",
    val nameTitle: String = "아이 이름",
    val relationTitle: String = "나와 아이의 관계",
    val relationDesc: String = "내가 추가한 아이들와 나의 관계입니다.",
    val birthTitle: String = "아이 생일 혹은 출산 예정일"
)