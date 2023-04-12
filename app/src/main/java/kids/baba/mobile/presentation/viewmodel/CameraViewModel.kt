package kids.baba.mobile.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Rational
import android.view.Surface
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kids.baba.mobile.domain.repository.PhotoPickerRepository
import kids.baba.mobile.domain.usecase.PhotoCaptureUseCase
import kids.baba.mobile.presentation.event.GetPictureEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val photoCaptureUseCase: PhotoCaptureUseCase,
    private val photoPickerRepository: PhotoPickerRepository,
    private val outputDirectory: File,
    private val cameraExecutor: Executor,
    /*private val imageAnalyzerExecutor: ExecutorService,
    private val imageAnalyzer: ImageAnalysis,*/
    private val preview: Preview,
    private val imageCapture: ImageCapture,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val TAG = "CameraViewModel"

    private val _eventFlow = MutableEventFlow<GetPictureEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var camera: Camera
    private lateinit var cameraProvider: ProcessCameraProvider
    private val viewPort = ViewPort.Builder(Rational(1, 1), Surface.ROTATION_0).build()


    fun takePhoto() {
        viewModelScope.launch {
            val fileName = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
                .format(System.currentTimeMillis()) + ".jpg"
            val photoFile = File(outputDirectory, fileName)
            Log.e(TAG, "photoFile: $photoFile")

            val metadata = ImageCapture.Metadata().apply {
                isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
            }

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                .apply {
                    setMetadata(metadata)
                }.build()

            photoCaptureUseCase(imageCapture, outputOptions, photoFile).catch {
                Log.e(TAG, it.message.toString())
                it.printStackTrace()
                throw it
            }.collect {
                _eventFlow.emit(GetPictureEvent.GetFromCamera(it))

            }
        }
    }

    fun setUpCamera(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            // 전면 후면 카메라 선택
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and Front camera are unavailable")
            }

            bindCameraUseCases(previewView, lifecycleOwner)

        }, cameraExecutor)
    }

    private fun bindCameraUseCases(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        val cameraSelector = CameraSelector.Builder().apply {
            requireLensFacing(lensFacing)
        }.build()

        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(preview)
            .addUseCase(imageCapture)
            .setViewPort(viewPort)
            .build()

        cameraProvider.unbindAll()
        try {
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                useCaseGroup
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }


    fun toggleCamera(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }
        bindCameraUseCases(previewView, lifecycleOwner)
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)
    }


    internal fun pickerSavePhoto(uri: Uri) {
        viewModelScope.launch {
            photoPickerRepository.getPhoto(uri).catch {
                Log.e(TAG, it.message.toString())
                throw it
            }.collect {
                _eventFlow.emit(GetPictureEvent.GetFromGallery(it))
            }
        }

    }


}

