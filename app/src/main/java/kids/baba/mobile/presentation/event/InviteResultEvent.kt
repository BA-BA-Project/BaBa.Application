package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class InviteResultEvent {
    object SuccessGetInvitationInfo : InviteResultEvent()
    object GoToMyPage : InviteResultEvent()
    object BackButtonClicked : InviteResultEvent()
    data class ShowSnackBar(@StringRes val message: Int) : InviteResultEvent()
}
