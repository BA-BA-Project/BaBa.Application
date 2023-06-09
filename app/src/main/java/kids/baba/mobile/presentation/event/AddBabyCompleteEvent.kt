package kids.baba.mobile.presentation.event

sealed class AddBabyCompleteEvent{

    object SuccessAddBaby : AddBabyCompleteEvent()
    object BackButtonClicked: AddBabyCompleteEvent()
}