package kids.baba.mobile.domain.model

data class BabyFamilyGroup(
    val groupName: String,
    val babies: List<Baby>,
    val members: List<Member>
)
