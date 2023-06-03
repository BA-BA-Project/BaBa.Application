package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class AddBabyEvent{
    object SuccessAddBaby : AddBabyEvent()
    data class ShowSnackBar(@StringRes val message: Int) : AddBabyEvent()
    object BackButtonClicked: AddBabyEvent()
}