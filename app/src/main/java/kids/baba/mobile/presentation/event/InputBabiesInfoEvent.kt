package kids.baba.mobile.presentation.event

sealed class InputBabiesInfoEvent {
    object InputEnd: InputBabiesInfoEvent()
    object SelectHaveInviteCode: InputBabiesInfoEvent()
    object InputText: InputBabiesInfoEvent()
}