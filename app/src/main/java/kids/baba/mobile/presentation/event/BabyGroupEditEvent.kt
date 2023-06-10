package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class BabyGroupEditEvent {

    object NameInput : BabyGroupEditEvent()
    data class SuccessBabyGroupEdit(val babyGroupTitle: String) : BabyGroupEditEvent()
    data class ShowSnackBar(@StringRes val message: Int) : BabyGroupEditEvent()
    object GoToAddBaby : BabyGroupEditEvent()
    object GoToInputInviteCode : BabyGroupEditEvent()
}
