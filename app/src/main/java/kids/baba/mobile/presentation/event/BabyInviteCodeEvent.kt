package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes
import kids.baba.mobile.domain.model.InviteCode

sealed class BabyInviteCodeEvent {

    data class InviteCodeInput(val isComplete: Boolean) : BabyInviteCodeEvent()
    data class SuccessAddBabyWithInviteCode(val inviteCode: InviteCode) : BabyInviteCodeEvent()
    object BackButtonClicked : BabyInviteCodeEvent()
    data class ShowSnackBar(@StringRes val message: Int) : BabyInviteCodeEvent()
}