package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class LoginEvent {
    data class ShowSnackBar(@StringRes val text: Int) : LoginEvent()
    object MoveToAgree : LoginEvent()
}