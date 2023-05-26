package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class EditGroupEvent{
    data class ShowSnackBar(@StringRes val message: Int) : EditGroupEvent()
    object SuccessPatchGroupRelation : EditGroupEvent()
    object SuccessDeleteGroup : EditGroupEvent()
}
