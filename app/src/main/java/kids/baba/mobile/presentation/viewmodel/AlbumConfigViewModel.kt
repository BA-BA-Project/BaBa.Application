package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.view.bottomsheet.AlbumConfigBottomSheet.Companion.NOW_ALBUM_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AlbumConfigViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _album = MutableStateFlow(savedStateHandle[NOW_ALBUM_KEY] ?: AlbumUiModel())
    val album = _album.asStateFlow()

}