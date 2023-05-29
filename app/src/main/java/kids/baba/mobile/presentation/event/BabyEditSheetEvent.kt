package kids.baba.mobile.presentation.event

sealed class BabyEditSheetEvent {
    object GoToAddBabyPage : BabyEditSheetEvent()
    object GoToInputInviteCodePage : BabyEditSheetEvent()
}
