package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class BabyListEvent{
    data class ShowSnackBar(@StringRes val message: Int): BabyListEvent()
}
