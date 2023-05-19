package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class AddBabyEvent{
    data class ShowSnackBar(@StringRes val text: Int) : AddBabyEvent()
    object SuccessAddBaby : AddBabyEvent()
}