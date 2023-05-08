package kids.baba.mobile.domain.model

data class Group(
    val groupName: String,
    val family: Boolean,
    val members: List<Member>?
)
