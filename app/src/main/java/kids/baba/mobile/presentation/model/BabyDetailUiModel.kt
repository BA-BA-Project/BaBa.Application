package kids.baba.mobile.presentation.model

data class BabyDetailUiModel(
    var babyName: String = "",
    var babyBirthday: String = "연동 필요",
    var familyGroupTitle: String = "가족 그룹 이름",
    val familyGroupDesc: String = "모든 그룹과 소식을 공유할 수 있어요",
    val myGroupTitle: String = "내가 속한 그룹 이름",
    val myGroupDesc: String = "[가족 그룹], [내가 속한 그룹]의 소식만 볼 수 있어요"
)