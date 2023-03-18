package kids.baba.mobile.presentation.view.film

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.BR
import kids.baba.mobile.databinding.FragmentCameraBinding
import kids.baba.mobile.presentation.viewmodel.CameraViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.Locale
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class CameraFragment @Inject constructor() : Fragment(), CameraNavigator {

    private val TAG = "CameraFragment"

    private var _binding: FragmentCameraBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val mDisplayManager by lazy {
        requireActivity().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    val viewModel: CameraViewModel by viewModels()

    private lateinit var mOutputDirectory: File
    private lateinit var mCameraExecutor: Executor
    private lateinit var mImageAnalyzerExecutor: ExecutorService
    private var mPreview: Preview? = null
    private var mImageAnalyzer: ImageAnalysis? = null
    private var mImageCapture: ImageCapture? = null
    private var mCamera: Camera? = null
    private var mCameraProvider: ProcessCameraProvider? = null
    private var mDisplayId: Int = -1
    private var mLensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var mFlashMode: Int = ImageCapture.FLASH_MODE_OFF


    @SuppressLint("RestrictedApi")
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.callback = this

        addListeners()
        mOutputDirectory = FilmActivity.getOutputDirectory(requireContext())

        mCameraExecutor = ContextCompat.getMainExecutor(requireActivity())

        mImageAnalyzerExecutor = Executors.newSingleThreadExecutor()
        mDisplayManager.registerDisplayListener(mDisplayListener, null)

        val viewFinder = binding.viewFinder
        viewFinder.post {
            mDisplayId = viewFinder.display.displayId
            setUpCamera()
        }




    }

    private fun addListeners() {

        val imageCaptureButton = binding.imageCaptureButton
        imageCaptureButton.setOnClickListener {
            takePhoto()
        }

    }

    private fun takePhoto() {
        val imageCapture = mImageCapture ?: return

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

        // Setup image capture listener which is triggered after photo has been taken
        imageCapture.takePicture(
            outputOptions,
            mCameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    val msg = "Photo capture failed: ${exc.message}"
//                    requireActivity().showToast(msg)
                    Log.e(TAG, msg, exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
//                    requireActivity().showToast(msg)
                    Log.d(TAG, msg)
                    val data = viewModel.savePhoto(savedUri.toString())
                    Log.e(TAG, data.toString())
                    Navigation.findNavController(requireActivity(), kids.baba.mobile.R.id.fcv_film)
                        .navigate(
                            CameraFragmentDirections.actionCameraFragmentToWriteTitleFragment(
                                data
                            )
                        )
                }
            })

    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        cameraProviderFuture.addListener(Runnable {
            mCameraProvider = cameraProviderFuture.get()

            // 전면, 후면 카메라 선택
            mLensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            mFlashMode = when {
                hasFlashMode() -> ImageCapture.FLASH_MODE_OFF
                else -> -1111 // NO_FLASH
            }


            bindCameraUseCases()
            buildUi()

        }, mCameraExecutor)
    }

    private fun buildUi() {
//        if (!hasFlashMode())
//            flashToggle.visibility = View.INVISIBLE
        if (!hasFrontCamera())
            binding.toggleScreenBtn.visibility = View.INVISIBLE
    }

    override fun gotoAlbum() {
        Log.e(TAG, "go to album")
//        TODO("Not yet implemented")
    }

    override fun toggleCamera() {
        mLensFacing = if (CameraSelector.LENS_FACING_FRONT == mLensFacing) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }

        bindCameraUseCases()

    }

    private fun bindCameraUseCases() {


        // CameraProvider
        val cameraProvider =
            mCameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().apply {
            requireLensFacing(mLensFacing)
        }.build()

        // Preview
        mPreview = Preview.Builder().apply {
        }.build()

        // ImageAnalysis
        mImageAnalyzer = ImageAnalysis.Builder().apply {
        }.build().also {
            it.setAnalyzer(mImageAnalyzerExecutor, LuminosityAnalyzer { luma ->
                Log.d(TAG, "Average luminosity: $luma")
            })
        }

        // ImageCapture
        mImageCapture = ImageCapture.Builder().apply {
            setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//            setTargetAspectRatio(screenAspectRatio)
//            setTargetRotation(rotation)
            if (mFlashMode != -1111)
                setFlashMode(mFlashMode)
        }.build()


        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            mCamera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                mPreview,
                mImageCapture,
            )
            // Attach the viewfinder's surface provider to preview use case
            mPreview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }

    }

    private fun hasBackCamera(): Boolean {
        return mCameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return mCameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    private fun hasFlashMode(): Boolean {
        return mCamera?.cameraInfo?.hasFlashUnit() ?: false
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }

}