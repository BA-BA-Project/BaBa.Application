package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class AddBabyCompleteEvent{

    object SuccessAddBaby : AddBabyCompleteEvent()
    object BackButtonClicked: AddBabyCompleteEvent()
}