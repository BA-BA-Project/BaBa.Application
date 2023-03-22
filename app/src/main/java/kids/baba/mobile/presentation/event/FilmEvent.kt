package kids.baba.mobile.presentation.event

import kids.baba.mobile.domain.model.MediaData

sealed class FilmEvent {

    object StartOnCamera: FilmEvent()
    data class MoveToCrop(val currentMedia: MediaData): FilmEvent()
    data class MoveToWriteTitle(val currentMedia: MediaData): FilmEvent()
    data class MoveToSelectCard(val currentMedia: MediaData): FilmEvent()

}