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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _lensFacing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    val lensFacing = _lensFacing.asStateFlow()

    private val _cameraProvider: MutableStateFlow<ProcessCameraProvider?> = MutableStateFlow(null)
    val cameraProvider = _cameraProvider.asStateFlow()

    private val _camera: MutableStateFlow<Camera?> = MutableStateFlow(null)
    val camera = _camera.asStateFlow()

    private val viewPort = ViewPort.Builder(Rational(1, 1), Surface.ROTATION_0).build()

    fun takePhoto() {
        viewModelScope.launch {
            val fileName = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
                .format(System.currentTimeMillis()) + ".jpg"
            val photoFile = File(outputDirectory, fileName)
            Log.e(TAG, "photoFile: $photoFile")

            val metadata = ImageCapture.Metadata().apply {
                isReversedHorizontal = lensFacing.value == CameraSelector.LENS_FACING_FRONT
            }

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                .apply {
                    setMetadata(metadata)
                }.build()

            photoCaptureUseCase.getMe(imageCapture, outputOptions, photoFile).catch {
                Log.e(TAG, it.message.toString())
                it.printStackTrace()
                throw it
            }.collect {
                _eventFlow.emit(GetPictureEvent.GetFromCamera(it))
                Log.d("CameraViewModel", "collect called $it")

            }
        }
    }


    fun setUpCamera(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            _cameraProvider.value = cameraProviderFuture.get()

            // 전면 후면 카메라 선택
            _lensFacing.value = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and Front camera are unavailable")
            }

            bindCameraUseCases(previewView, lifecycleOwner)

        }, cameraExecutor)
    }

    private fun bindCameraUseCases(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        if (cameraProvider.value == null) {
            throw IllegalStateException("Camera Initialization failed")
        }
        val cameraSelector = CameraSelector.Builder().apply {
            requireLensFacing(lensFacing.value)
        }.build()

        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(preview)
            .addUseCase(imageCapture)
            .setViewPort(viewPort)
            .build()

        cameraProvider.value!!.unbindAll()
        try {
            _camera.value = cameraProvider.value!!.bindToLifecycle(
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
        _lensFacing.value = if (CameraSelector.LENS_FACING_FRONT == lensFacing.value) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }
        bindCameraUseCases(previewView, lifecycleOwner)
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider.value?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider.value?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
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

