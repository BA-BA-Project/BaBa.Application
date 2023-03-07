package kids.baba.mobile.presentation.event


sealed class SignUpEvent {
    object WaitGreeting : SignUpEvent()
    object WaitName : SignUpEvent()
    object WaitProfile : SignUpEvent()
    object EndCreateProfile : SignUpEvent()
}
