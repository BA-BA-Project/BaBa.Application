package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.ClassifiedAlbumList
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ClassifiedAlbumDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val itemId = savedStateHandle.get<Int>(ITEM_ID) ?: -1
    val albumList = MutableStateFlow(savedStateHandle[CLASSIFIED_ALBUM_LIST] ?: ClassifiedAlbumList(listOf()))


    companion object {
        const val ITEM_ID = "itemId"
        const val CLASSIFIED_ALBUM_LIST = "classified_album_list"
    }

}