package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.BuildConfig
import kids.baba.mobile.R
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.AlbumDeleteUseCase
import kids.baba.mobile.domain.usecase.DownloadPhotoUseCase
import kids.baba.mobile.presentation.event.AlbumConfigEvent
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.bottomsheet.AlbumConfigBottomSheet.Companion.NOW_ALBUM_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumConfigViewModel @Inject constructor(
    val albumDeleteUseCase: AlbumDeleteUseCase,
    val downloadPhotoUseCase: DownloadPhotoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _album = MutableStateFlow(savedStateHandle[NOW_ALBUM_KEY] ?: AlbumUiModel())
    val album = _album.asStateFlow()

    private val _eventFlow = MutableEventFlow<AlbumConfigEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val baby = EncryptedPrefs.getBaby(PrefsKey.BABY_KEY)

    fun albumDelete() = viewModelScope.launch {
        val contentId = album.value.contentId
        if (contentId != null) {
            when (albumDeleteUseCase(baby.babyId, contentId)) {
                is Result.Success -> _eventFlow.emit(AlbumConfigEvent.DeleteAlbum)
                is Result.NetworkError -> _eventFlow.emit(AlbumConfigEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(AlbumConfigEvent.ShowSnackBar(R.string.baba_album_delete_failed))
            }
        }
    }

    fun reportAlbum() {
        // 신고 기능은 UI로만 남기고 미구현
    }

    fun savePhoto() = viewModelScope.launch{
        withContext(Dispatchers.IO){
            val fileUrl = album.value.photo.substringAfter(BuildConfig.BASE_PHOTO_URL)
            when(val result = downloadPhotoUseCase(fileUrl, "${baby.name}_${album.value.date}")){
                is Result.Success -> _eventFlow.emit(AlbumConfigEvent.ShowDownSuccessNotification(result.data))
                is Result.NetworkError ->_eventFlow.emit(AlbumConfigEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(AlbumConfigEvent.ShowSnackBar(R.string.baba_photo_download_failed))
            }
        }
    }

    fun checkDeleteAlbum() = viewModelScope.launch {
        _eventFlow.emit(AlbumConfigEvent.ShowDeleteCheckDialog)
    }

    fun checkReportAlbum() = viewModelScope.launch {
        _eventFlow.emit(AlbumConfigEvent.ShowReportCheckDialog)
    }
}