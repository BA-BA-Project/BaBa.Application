package kids.baba.mobile.domain.usecase

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import kids.baba.mobile.domain.model.MediaData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.util.concurrent.Executor
import javax.inject.Inject

class PhotoCaptureUseCase @Inject constructor(
    private val cameraExecutor: Executor
) {
    private val TAG = "PhotoCaptureUseCase"

    suspend operator fun invoke(
        imageCapture: ImageCapture,
        outputOptions: ImageCapture.OutputFileOptions
    ) = callbackFlow {
        Log.e(TAG, "getMe Called")
        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.e(TAG, "onImageSaved Called")
                    val data = savePhoto(outputFileResults.savedUri.toString())
                    trySendBlocking(data)
                }

                override fun onError(exception: ImageCaptureException) {
                    val msg = "Photo capture failed: ${exception.message}"
                    exception.printStackTrace()
                    Log.e(TAG, msg)
                }
            }
        )
        awaitClose()
    }

    private fun savePhoto(path: String): MediaData {
        val file = File(path)

        return MediaData(
            mediaName = file.name,
            mediaUri = path
        )
    }
}