package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class BabyEditEvent{

    object NameInput : BabyEditEvent()
    data class SuccessBabyEdit(val babyName: String) : BabyEditEvent()
    data class ShowSnackBar(@StringRes val message: Int) : BabyEditEvent()
}
