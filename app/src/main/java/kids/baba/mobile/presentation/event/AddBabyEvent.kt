package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class AddBabyEvent{

    object NameInputEnd : AddBabyEvent()
    object RelationInputEnd : AddBabyEvent()
    object SuccessAddBaby : AddBabyEvent()
    data class ShowSnackBar(@StringRes val message: Int) : AddBabyEvent()
    object BackButtonClicked: AddBabyEvent()
}