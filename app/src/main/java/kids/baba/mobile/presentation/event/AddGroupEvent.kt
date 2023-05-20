package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class AddGroupEvent{
    object SuccessAddGroup: AddGroupEvent()
    data class ShowSnackBar(@StringRes val message: Int): AddGroupEvent()
}
