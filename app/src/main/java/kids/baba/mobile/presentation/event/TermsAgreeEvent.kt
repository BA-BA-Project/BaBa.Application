package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class TermsAgreeEvent {

    data class ShowSnackBar(@StringRes val text: Int) : TermsAgreeEvent()
}