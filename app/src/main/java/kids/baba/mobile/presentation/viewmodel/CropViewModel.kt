package kids.baba.mobile.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canhub.cropper.CropImageView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kids.baba.mobile.domain.model.MediaData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CropViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val TAG = "CropViewModel"

    private var currentTakenMedia = savedStateHandle.get<MediaData>(MEDIA_DATA)

    fun setCropFrame(cropImageView: CropImageView) {
        cropImageView.apply {
            setAspectRatio(1, 1)
            setFixedAspectRatio(true)
            guidelines = CropImageView.Guidelines.ON
            cropShape = CropImageView.CropShape.RECTANGLE
            scaleType = CropImageView.ScaleType.FIT_CENTER
            isAutoZoomEnabled = false
            isShowProgressBar = true
        }
        cropImageView.setImageUriAsync(currentTakenMedia!!.mediaUri/*Uri.parse(currentTakenMedia!!.mediaPath)*/)
    }

    fun cropImage(cropImageView: CropImageView) = callbackFlow {
        viewModelScope.launch {
            if (currentTakenMedia != null) {
                cropImageView.setOnCropImageCompleteListener { _, result ->
                    Log.d(
                        TAG, "CropResult - original uri : ${result.originalUri}" +
                                "  cropped content: ${result.uriContent}"
                    )

                    val fileName = result.uriContent.toString().split("/").last()
                    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val file = File(storageDir, fileName)

                    currentTakenMedia =
                        MediaData(
                            mediaName = "Cropped",
                            mediaDate = currentTakenMedia!!.mediaDate,
                            mediaUri = file.absolutePath.toUri()
                        )

                    Log.d(TAG, "currentTakenMedia: $currentTakenMedia!!")
                    trySendBlocking(currentTakenMedia!!)
                }
                cropImageView.croppedImageAsync()
            }

        }
        awaitClose()
    }

    companion object {
        const val MEDIA_DATA = "mediaData"
    }

}



