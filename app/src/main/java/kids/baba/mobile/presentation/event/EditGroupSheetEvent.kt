package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class EditGroupSheetEvent {
    object GoToAddMemberPage : EditGroupSheetEvent()
    data class ShowSnackBar(@StringRes val message: Int) : EditGroupSheetEvent()
    object SuccessPatchGroupRelation : EditGroupSheetEvent()
    object SuccessDeleteGroup : EditGroupSheetEvent()
}
