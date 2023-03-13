package kids.baba.mobile.presentation.event

import kids.baba.mobile.presentation.model.UserProfile


sealed class CreateProfileEvent {
    object WaitGreeting : CreateProfileEvent()
    object WaitName : CreateProfileEvent()
    object ProfileSelectLoading : CreateProfileEvent()
    object EndCreateProfile : CreateProfileEvent()
    data class MoveToInputChildInfo(val userProfile: UserProfile): CreateProfileEvent()
}
