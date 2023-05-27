package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class BabyInviteCodeEvent {
    object SuccessAddBabyWithInviteCode : BabyInviteCodeEvent()
    object BackButtonClicked : BabyInviteCodeEvent()
    data class ShowSnackBar(@StringRes val message: Int) : BabyInviteCodeEvent()
}