package kids.baba.mobile.presentation.viewmodel

import android.app.Application
import android.content.res.Resources
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.domain.repository.PhotoPickerRepository
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.PhotoCaptureUseCase
import kids.baba.mobile.presentation.event.FilmEvent
import kids.baba.mobile.presentation.event.GetPictureEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.film.LuminosityAnalyzer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val photoCaptureUseCase: PhotoCaptureUseCase,
    private val photoPickerRepository: PhotoPickerRepository,
    private val outputDirectory: File,
    private val cameraExecutor: Executor,
    private val imageAnalyzerExecutor: ExecutorService,
    private val preview: Preview,
    private val imageCapture: ImageCapture,
    application: Application,
) : BaseViewModel(application) {

    private val TAG = "CameraViewModel"

    private var mLensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var mCameraProvider: ProcessCameraProvider? = null
    private var mImageAnalyzer: ImageAnalysis? = null
    private var mCamera: Camera? = null

    private val _eventFlow = MutableEventFlow<GetPictureEvent>()
    val eventFlow = _eventFlow.asEventFlow()


    fun takePhoto() {
        viewModelScope.launch {
            val fileName = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
                .format(System.currentTimeMillis()) + "jpg"
            val dateInfo = SimpleDateFormat("yy-MM-dd", Locale.KOREA).format(System.currentTimeMillis())
            val photoFile = File(outputDirectory, fileName)

            val metadata = ImageCapture.Metadata().apply {
                isReversedHorizontal = mLensFacing == CameraSelector.LENS_FACING_FRONT
            }

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                .apply {
                    setMetadata(metadata)
                }.build()

            photoCaptureUseCase.getMe(imageCapture, outputOptions, photoFile, dateInfo).catch {
                Log.e(TAG, it.message.toString())
                throw it
            }.collect {
                _eventFlow.emit(GetPictureEvent.getFromCamera(it))
                Log.e("CameraViewModel", "collect called $it")

            }
        }
    }


    fun setUpCamera(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(Runnable {
            mCameraProvider = cameraProviderFuture.get()

            // 전면 후면 카메라 선택
            mLensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and Front camera are unavailable")
            }

            bindCameraUseCases(previewView, lifecycleOwner)

        }, cameraExecutor)
    }

    private fun bindCameraUseCases(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        val cameraProvider = mCameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector = CameraSelector.Builder().apply {
            requireLensFacing(mLensFacing)
        }.build()

        mImageAnalyzer = ImageAnalysis.Builder().apply { }.build().also {
            it.setAnalyzer(imageAnalyzerExecutor, LuminosityAnalyzer { luma ->
                Log.d(TAG, "Average luminosity: $luma")
            })
        }
        cameraProvider.unbindAll()
        try {
            mCamera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    //
    fun toggleCamera(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        mLensFacing = if (CameraSelector.LENS_FACING_FRONT == mLensFacing) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }
        bindCameraUseCases(previewView, lifecycleOwner)
    }

    private fun hasBackCamera(): Boolean {
        return mCameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return mCameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }




    internal fun pickerSavePhoto(uri: Uri) {

        viewModelScope.launch {
            photoPickerRepository.getPhoto(uri).catch {
                Log.e(TAG, it.message.toString())
                throw it
            }.collect{
                _eventFlow.emit(GetPictureEvent.getFromGallery(it))
            }
        }

    }


}

