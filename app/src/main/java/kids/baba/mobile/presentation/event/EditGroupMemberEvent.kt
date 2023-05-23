package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class EditGroupMemberEvent{
    object SuccessPatchMemberRelation : EditGroupMemberEvent()
    object SuccessDeleteMember : EditGroupMemberEvent()
    data class ShowSnackBar(@StringRes val message: Int) : EditGroupMemberEvent()
}