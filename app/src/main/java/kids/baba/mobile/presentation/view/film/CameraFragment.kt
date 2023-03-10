package kids.baba.mobile.presentation.view.film

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kids.baba.mobile.databinding.FragmentCameraBinding
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.Locale

typealias LumaListener = (luma: Double) -> Unit


class CameraFragment : Fragment() {

    private val tag = "CameraXApp"
    private var _binding: FragmentCameraBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()

        binding.imageCaptureButton.setOnClickListener { takePhoto() }
        cameraExecutor = Executors.newSingleThreadExecutor()

    }



    private fun startCamera() {
        // used to bind the lifecycle of camera to the lifecycle owner
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }
                imageCapture = ImageCapture.Builder().build()

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
//                            Log.d(tag, "Average luminosity: $luma")
                        })
                    }


                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture, imageAnalyzer
                    )
                } catch (exc: Exception) {
                    Log.e(tag, "Use case binding failed", exc)
                }
            },
            // return Executor (runs on main thread
            ContextCompat.getMainExecutor(requireContext())
        )


    }

    private fun takePhoto() {

        // get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // time stamped name , MediaStore entry
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA)
            .format(System.currentTimeMillis())

        // 이미지 저장할 공간 (mediaStore)
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures//CameraX-Image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(requireContext().contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            .build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${outputFileResults.savedUri}"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    Log.d(tag, msg)

                    // TODO: 제목입력 프래그먼트로 이동

                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(tag, "Photo capture failed : ${exception.message}", exception)
                }

            }
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
        cameraExecutor.shutdown()

    }


}

/**
 * ImageAnalyzer
 */
private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {
    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind() // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data) // copy the buffer into a byte array
        return data
    }

    override fun analyze(image: ImageProxy) {
        val buffer = image.planes[0].buffer

        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luma = pixels.average()

        listener(luma)

        image.close()
    }


}