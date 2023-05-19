package kids.baba.mobile.presentation.event

import kids.baba.mobile.presentation.model.ClassifiedAlbumList

sealed class GatheringAlbumEvent {

    object Initialized : GatheringAlbumEvent()
    data class GoToClassifiedAllAlbum(
        val itemId: Int,
        val classifiedAlbumList: ClassifiedAlbumList,
        val fromMonth: Boolean
    ) : GatheringAlbumEvent()
}