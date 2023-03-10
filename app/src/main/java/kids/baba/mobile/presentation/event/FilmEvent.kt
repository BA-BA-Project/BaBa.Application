package kids.baba.mobile.presentation.event

import android.net.Uri
import kids.baba.mobile.domain.model.SavedImg

sealed class FilmEvent {

    object OnStartCamera: FilmEvent()
    object MoveToGallery: FilmEvent()
    data class MoveToCrop(val savedImg: SavedImg) : FilmEvent() // 여기에는 사진 데이터를 가져와야 함
    data class MoveToWriteTitle(val savedImg: SavedImg) : FilmEvent() // 여기에도 사진 데이터를 가져와야 함.

}