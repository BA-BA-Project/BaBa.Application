package kids.baba.mobile.presentation.event

import kids.baba.mobile.presentation.model.UserProfile


sealed class SignUpEvent {
    object WaitGreeting : SignUpEvent()
    object WaitName : SignUpEvent()
    object ProfileSelectLoading : SignUpEvent()
    object EndCreateProfile : SignUpEvent()
    data class MoveToInputChildInfo(val userProfile: UserProfile): SignUpEvent()
}
