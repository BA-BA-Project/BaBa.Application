package kids.baba.mobile.presentation.event

import kids.baba.mobile.domain.model.MediaData

sealed class GetPictureEvent {
    data class getFromCamera(val mediaData: MediaData) : GetPictureEvent()
    data class getFromGallery(val mediaData: MediaData) : GetPictureEvent()
}
