package kids.baba.mobile.presentation.event

import kids.baba.mobile.presentation.model.UserProfile

sealed class IntroEvent {
    object MoveToLogin : IntroEvent()
    data class MoveToAgree(val socialToken: String) : IntroEvent()
    data class MoveToCreateUserProfile(val signToken: String) : IntroEvent()
    data class MoveToInputBabiesInfo(val signToken: String, val userProfile: UserProfile) : IntroEvent()
    data class MoveToWelcome(val name: String): IntroEvent()

}