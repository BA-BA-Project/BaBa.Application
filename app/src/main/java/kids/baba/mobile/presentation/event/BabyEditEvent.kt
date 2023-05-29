package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class BabyEditEvent{
    object SuccessBabyEdit: BabyEditEvent()
    data class ShowSnackBar(@StringRes val message: Int): BabyEditEvent()
}
