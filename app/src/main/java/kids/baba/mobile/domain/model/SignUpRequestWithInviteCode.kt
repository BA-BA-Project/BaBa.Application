package kids.baba.mobile.domain.model

data class SignUpRequestWithInviteCode(
    val inviteCode: String,
    val name: String,
    val iconName: String
)
