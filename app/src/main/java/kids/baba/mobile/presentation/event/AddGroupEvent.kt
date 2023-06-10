package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class AddGroupEvent {

    data class RelationInput(val isComplete: Boolean) : AddGroupEvent()
    object SuccessAddGroup : AddGroupEvent()
    data class ShowSnackBar(@StringRes val message: Int) : AddGroupEvent()
    object GoToBack : AddGroupEvent()
}
