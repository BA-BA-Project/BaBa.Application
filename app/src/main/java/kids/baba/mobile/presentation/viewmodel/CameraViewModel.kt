package kids.baba.mobile.presentation.viewmodel

import android.app.Application
import android.hardware.display.DisplayManager
import android.net.Uri
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.presentation.event.FilmEvent
import kids.baba.mobile.presentation.util.flow.EventFlow
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.film.CameraFragment
import kids.baba.mobile.presentation.view.film.FilmActivity
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
open class CameraViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val TAG = "CameraViewModel"

    protected val context get() = getApplication<Application>()

    private val _eventFlow = MutableStateFlow<FilmEvent>(FilmEvent.StartOnCamera)
    val eventFlow: StateFlow<FilmEvent> = _eventFlow


    private lateinit var mOutputDirectory: File
    internal lateinit var mCameraExecutor: Executor
    internal lateinit var mImageAnalyzerExecutor: ExecutorService
    internal var mImageAnalyzer: ImageAnalysis? = null
    internal var mImageCapture: ImageCapture? = null
    internal var mLensFacing: Int = CameraSelector.LENS_FACING_BACK

    /*
    private val mDisplayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) {
            if (displayId == mDisplayId) {
                val viewFinder = binding.viewFinder
                mImageAnalyzer?.targetRotation = viewFinder.display.rotation
                mImageCapture?.targetRotation = viewFinder.display.rotation
            }
        }
    }
     */

    fun init() {
        mOutputDirectory = FilmActivity.getOutputDirectory(context)
        mCameraExecutor = ContextCompat.getMainExecutor(context)
        mImageAnalyzerExecutor = Executors.newSingleThreadExecutor()
    }

    fun savePhoto(path: String) = flow<FilmEvent> {
        viewModelScope.launch {
            val file = File(path)
            val mediaData = MediaData(
                mediaName = file.name,
                mediaType = "IMAGE",
                mediaSize = file.length(),
                mediaPath = path
            )

//            _eventFlow.emit(FilmEvent.MoveToWriteTitle(mediaData))
        }

    }

    fun takePhoto() {
        Log.e(TAG, "takePhoto() called")
        viewModelScope.launch {

            savePic()
        }
    }


    suspend fun savePic() {
        val imageCapture = mImageCapture

        // Create timestamped output file to hold the image
        val fileName = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis()) + ".jpg"
        val photoFile = File(mOutputDirectory, fileName)

        val metadata = ImageCapture.Metadata().apply {
            // Mirror image when using the front camera
            isReversedHorizontal = mLensFacing == CameraSelector.LENS_FACING_FRONT
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .apply {
                setMetadata(metadata)
            }.build()


        if (imageCapture != null) {
            Log.e(TAG, "imageCapture is not null")
            imageCapture.takePicture(
                outputOptions,
                mCameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(photoFile)
                        val msg = "Photo capture succeeded : $savedUri"
                        Log.e(TAG, msg)

                        viewModelScope.launch {
                            launch {
                                _eventFlow.collect()
                            }

                            Log.e(TAG, "viewModelScope.launch")
                            val savedUri = Uri.fromFile(photoFile)

                            val file = File(savedUri.toString())
                            val mediaData = MediaData(
                                mediaName = file.name,
                                mediaType = "IMAGE",
                                mediaSize = file.length(),
                                mediaPath = savedUri.toString()
                            )

                            Log.e(
                                TAG,
                                "${mediaData.mediaName} , ${mediaData.mediaType}  , " +
                                        "${mediaData.mediaSize}  ,  ${mediaData.mediaPath}"
                            )
//                            trySendBlocking(FilmEvent.MoveToWriteTitle(mediaData))
                            _eventFlow.emit(FilmEvent.MoveToWriteTitle(mediaData))

                            Log.e(TAG, "_eventFlow. emit")
                        }

//                        Navigation.findNavController(requireActivity(), kids.baba.mobile.R.id.fcv_film)
//                            .navigate(
//                                CameraFragmentDirections.actionCameraFragmentToWriteTitleFragment(
//                                    data
//                                )
//                            )

                    }

                    override fun onError(exception: ImageCaptureException) {
                        val msg = "Photo capture failed: ${exception.message}"
//                    requireActivity().showToast(msg)
                        Log.e(TAG, msg, exception)
                    }

                }
            )
        } else {
            Log.e(TAG, "imageCapture is null")
        }

    }

}