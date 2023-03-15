package kids.baba.mobile.presentation.event

import kids.baba.mobile.domain.model.MediaData

sealed class FilmEvent {

    object StartOnCamera: FilmEvent()
    data class MoveToWriteTitle(val mediaData: MediaData): FilmEvent()
    object MoveToGallery: FilmEvent()

}