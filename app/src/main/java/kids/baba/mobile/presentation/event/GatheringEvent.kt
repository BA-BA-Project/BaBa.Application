package kids.baba.mobile.presentation.event

sealed class GatheringEvent {

    object Initialized: GatheringEvent()
    data class GoToClassified(val itemId: Int): GatheringEvent()

}