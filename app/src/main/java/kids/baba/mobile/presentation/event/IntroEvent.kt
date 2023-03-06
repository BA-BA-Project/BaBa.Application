package kids.baba.mobile.presentation.event

import kids.baba.mobile.domain.model.MemberModel

sealed class IntroEvent {
    object StartOnBoarding : IntroEvent()
    object MoveToLogin : IntroEvent()
    data class MoveToAgree(val signToken: String) : IntroEvent()
    data class MoveToSignUp(val signToken: String) : IntroEvent()
    data class MoveToMain(val member: MemberModel) : IntroEvent()
}