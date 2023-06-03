package kids.baba.mobile.domain.model

data class Baby(
    val babyId : String,
    val groupColor: String,
    val name: String,
    val isMyBaby: Boolean
){
    fun toMember() = MemberModel(
    memberId = babyId,
    name = name,
    introduction = "아이",
    iconName = "PROFILE_BABY_1",
    iconColor = groupColor
    )
}
//"babyId": "asdasdasd",
//"groupColor": "#3481FF",
//"name": "앙쥬1"
