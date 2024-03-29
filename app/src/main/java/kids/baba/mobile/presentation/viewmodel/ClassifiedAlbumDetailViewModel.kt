package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.ClassifiedAlbumList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ClassifiedAlbumDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val itemId = savedStateHandle.get<Int>(ITEM_ID) ?: -1
    private val _albumList = MutableStateFlow(savedStateHandle[CLASSIFIED_ALBUM_LIST] ?: ClassifiedAlbumList(listOf()))
    val albumList = _albumList.asStateFlow()
    val fromMonth = savedStateHandle.get<Boolean>(FROM_MONTH) ?: true
    val date = albumList.value.classifiedAlbumList[0].date

    val monthDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
    val yearDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy")

    fun sort() {
        _albumList.update {
            it.copy(
                classifiedAlbumList = it.classifiedAlbumList.reversed()
            )
        }
    }

    fun deleteAlbum(album: AlbumUiModel) {
        _albumList.update {
            it.copy(
                classifiedAlbumList = it.classifiedAlbumList.filter { classifiedAlbum ->
                    classifiedAlbum != album
                }
            )
        }
    }

    companion object {
        const val ITEM_ID = "itemId"
        const val CLASSIFIED_ALBUM_LIST = "classified_album_list"
        const val FROM_MONTH = "fromMonth"
    }

}