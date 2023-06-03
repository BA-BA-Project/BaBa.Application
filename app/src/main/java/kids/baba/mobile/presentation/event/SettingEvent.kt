package kids.baba.mobile.presentation.event

sealed class SettingEvent{
    object GoToServiceInfo: SettingEvent()
    object GoToDeleteMember: SettingEvent()
    object GoToAsk: SettingEvent()
    object GoToCreator: SettingEvent()
    object BackButtonClicked: SettingEvent()
}
