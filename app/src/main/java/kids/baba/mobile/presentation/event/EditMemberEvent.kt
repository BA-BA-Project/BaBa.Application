package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class EditMemberEvent{
    object SuccessEditMember : EditMemberEvent()
    data class ShowSnackBar(@StringRes val message: Int) : EditMemberEvent()
}
