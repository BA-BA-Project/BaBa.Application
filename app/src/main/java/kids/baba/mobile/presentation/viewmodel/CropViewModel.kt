package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canhub.cropper.CropImageView
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MediaData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val TAG = "CropViewModel"

    val currentTakenMediaInCrop = savedStateHandle.get<MediaData>(MEDIA_DATA)
    private val currentTakenMedia =
        MutableStateFlow(savedStateHandle[MEDIA_DATA] ?: MediaData())


    fun cropImage(cropImageView: CropImageView) = callbackFlow {
        viewModelScope.launch {
            cropImageView.setOnCropImageCompleteListener { _, result ->
                Log.d(
                    TAG, "CropResult - original uri : ${result.originalUri}" +
                            "  cropped content: ${result.uriContent}"
                )

                currentTakenMedia.update {
                    it.copy(mediaName = "Cropped", mediaUri = result.uriContent.toString())
                }

                trySendBlocking(currentTakenMedia.value)
            }
            cropImageView.croppedImageAsync()

        }
        awaitClose()
    }

    companion object {
        const val MEDIA_DATA = "mediaData"
    }

}