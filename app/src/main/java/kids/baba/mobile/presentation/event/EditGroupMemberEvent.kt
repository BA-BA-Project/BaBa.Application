package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class EditGroupMemberEvent{

    object RelationInput : EditGroupMemberEvent()
    object SuccessPatchMemberRelation : EditGroupMemberEvent()
    object SuccessDeleteMember : EditGroupMemberEvent()
    object DismissDialog : EditGroupMemberEvent()
    data class ShowSnackBar(@StringRes val message: Int) : EditGroupMemberEvent()
}
