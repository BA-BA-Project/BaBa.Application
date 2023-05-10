package kids.baba.mobile.presentation.event

import kids.baba.mobile.presentation.model.ClassifiedAlbumList

sealed class GatheringEvent {

    object Initialized : GatheringEvent()
    data class GoToClassifiedByMonth(val itemId: Int, val classifiedAlbumList: ClassifiedAlbumList, val fromMonth: Boolean) : GatheringEvent()
    data class GoToClassifiedByYear(val itemId: Int, val classifiedAlbumList: ClassifiedAlbumList, val fromMonth: Boolean) : GatheringEvent()
}