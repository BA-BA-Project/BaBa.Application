package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class EditMemberProfileEvent{

    object NameInput : EditMemberProfileEvent()

    object IntroInput : EditMemberProfileEvent()
    object SuccessEditMemberProfile : EditMemberProfileEvent()
    data class ShowSnackBar(@StringRes val message: Int) : EditMemberProfileEvent()
}
