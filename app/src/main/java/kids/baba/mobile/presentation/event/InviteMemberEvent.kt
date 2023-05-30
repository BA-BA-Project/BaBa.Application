package kids.baba.mobile.presentation.event

sealed class InviteMemberEvent {
    object GoToBack : InviteMemberEvent()
    object InviteWithKakao : InviteMemberEvent()
    object CopyInviteCode : InviteMemberEvent()
}
