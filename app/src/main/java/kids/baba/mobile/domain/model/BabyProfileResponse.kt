package kids.baba.mobile.domain.model

data class BabyProfileResponse(
    val birthday: String,
    val familyGroup: BabyFamilyGroup,
    val myGroup: BabyMyGroup
)
