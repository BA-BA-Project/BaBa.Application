package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes
import kids.baba.mobile.domain.model.InviteCode

sealed class InviteMemberEvent {
    data class SuccessCopyInviteCode(val inviteCode: InviteCode) : InviteMemberEvent()
    data class ShowSnackBar(@StringRes val msg: Int) : InviteMemberEvent()
}
