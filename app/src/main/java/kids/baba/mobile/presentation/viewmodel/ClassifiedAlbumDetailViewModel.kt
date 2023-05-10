package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClassifiedAlbumDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {


    val itemId = savedStateHandle.get<Int>(ITEM_ID) ?: -1


    companion object {
        const val ITEM_ID = "itemId"
    }

}