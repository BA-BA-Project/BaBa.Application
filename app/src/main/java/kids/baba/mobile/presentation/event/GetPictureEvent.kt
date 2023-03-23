package kids.baba.mobile.presentation.event

import kids.baba.mobile.domain.model.MediaData

sealed class GetPictureEvent {
    data class GetFromCamera(val mediaData: MediaData) : GetPictureEvent()
    data class GetFromGallery(val mediaData: MediaData) : GetPictureEvent()
}
