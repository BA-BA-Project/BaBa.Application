package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class AddBabyEvent{

    data class NameInputEvent(val isComplete: Boolean) : AddBabyEvent()
    data class RelationInputEvent(val isComplete: Boolean) : AddBabyEvent()
    object SuccessAddBaby : AddBabyEvent()
    data class ShowSnackBar(@StringRes val message: Int) : AddBabyEvent()
    object BackButtonClicked : AddBabyEvent()
}