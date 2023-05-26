package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class BabyGroupEditEvent{
    object SuccessBabyGroupEdit: BabyGroupEditEvent()
    data class ShowSnackBar(@StringRes val message: Int): BabyGroupEditEvent()
    object GoToAddBaby: BabyGroupEditEvent()
    object GoToInputInviteCode: BabyGroupEditEvent()
}
