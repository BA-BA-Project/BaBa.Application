package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.AlbumUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectCardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
): ViewModel(){

    val album = MutableStateFlow<AlbumUiModel?>(null)
    init {
        getAlbumDetail()
    }

    private fun getAlbumDetail(){
        val tempAlbum = AlbumUiModel(
            contentId = 111,
            name = "박재희",
            relation = "엄마",
            date = "21-09-28",
            title = "앨범 테스트",
            like = false,
            photo = "https://www.shutterstock.com/image-photo/cute-little-african-american-infant-600w-1937038210.jpg",
            cardStyle = "test"
        )

        album.value = tempAlbum
    }

}