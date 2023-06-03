package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes
import kids.baba.mobile.domain.model.BabiesInfoResponse

sealed class InviteResultEvent {
    data class SuccessGetInvitationInfo(val data: BabiesInfoResponse) : InviteResultEvent()
    object SuccessGetInvitation : InviteResultEvent()
    object GoToMyPage : InviteResultEvent()
    object BackButtonClicked : InviteResultEvent()
    data class ShowSnackBar(@StringRes val message: Int) : InviteResultEvent()
}
