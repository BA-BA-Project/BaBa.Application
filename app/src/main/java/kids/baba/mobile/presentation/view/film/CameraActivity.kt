//package kids.baba.mobile.presentation.view.film
//
//import android.annotation.SuppressLint
//import android.content.ContentValues
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.provider.MediaStore
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.camera.core.*
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import kids.baba.mobile.databinding.ActivityCameraBinding
//import java.nio.ByteBuffer
//import java.text.SimpleDateFormat
//import java.util.Locale
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
////typealias LumaListener = (luma: Double) -> Unit
//
//class CameraActivity : AppCompatActivity() {
//    private lateinit var viewBinding: ActivityCameraBinding
//
//    private var imageCapture: ImageCapture? = null
//
//    private lateinit var cameraExecutor: ExecutorService
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
//        setContentView(viewBinding.root)
//
//        /*
//        // Request camera permissions
//        if (allPermissionsGranted()) {
//            startCamera()
//        } else {
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
//        }
//         */
//        startCamera()
//
//        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
//        cameraExecutor = Executors.newSingleThreadExecutor()
//
//
//    }
//
//    private fun takePhoto() {
//
//        // get a stable reference of the modifiable image capture use case
//        val imageCapture = imageCapture ?: return
//
//        // time stamped name , MediaStore entry
//        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
//            .format(System.currentTimeMillis())
//
//        // 이미지 저장할 공간 (mediaStore)
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures//CameraX-Image")
//            }
//        }
//
//        val outputOptions = ImageCapture.OutputFileOptions
//            .Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//            .build()
//
//        imageCapture.takePicture(
//            outputOptions, ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    val msg = "Photo capture succeeded: ${outputFileResults.savedUri}"
//                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, msg)
//
//                    // TODO: 제목입력 액티비티로 이동
//
//
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    Log.e(TAG, "Photo capture failed : ${exception.message}", exception)
//                }
//
//            }
//        )
//    }
//
//    private fun startCamera() {
//
//        // used to bind the lifecycle of camera to the lifecycle owner
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//
//        cameraProviderFuture.addListener(
//            {
//                // used tot bind the lifecycle of camera to the lifecycle owner
//                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//                // Preview
//                val preview = Preview.Builder()
//                    .build()
//                    .also {
//                        it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
//                    }
//                imageCapture = ImageCapture.Builder().build()
//
//                val imageAnalyzer = ImageAnalysis.Builder()
//                    .build()
//                    .also {
//                        it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
////                            Log.d(TAG, "Average luminosity: $luma")
//                        })
//                    }
//
//
//                // select back camera as a default
//                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//                try {
//                    // unbind use cases before rebinding
//                    cameraProvider.unbindAll()
//
//                    // bind use cases to camera
//                    cameraProvider.bindToLifecycle(
//                        this, cameraSelector, preview, imageCapture, imageAnalyzer
//                    )
//                } catch (exc: Exception) {
//                    // focus 가 없으면 실패 -> 예외 처리
//                    Log.e(TAG, "Use case binding failed", exc)
//                }
//            },
//            // return Executor (runs on main thread)
//            ContextCompat.getMainExecutor(this)
//        )
//
//    }
//
////    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
////        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
////    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraExecutor.shutdown()
//    }
//
////    @SuppressLint("MissingSuperCall")
////    override fun onRequestPermissionsResult(
////        requestCode: Int, permissions: Array<String>, grantResults:
////        IntArray
////    ) {
////        if (requestCode == REQUEST_CODE_PERMISSIONS) {
////            if (allPermissionsGranted()) {
////                startCamera()
////            } else {
////                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
////                finish()
////            }
////        }
////    }
//
//
////    companion object {
////        private const val TAG = "CameraXApp"
////        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
////        private const val REQUEST_CODE_PERMISSIONS = 10
////        private val REQUIRED_PERMISSIONS =
////            mutableListOf(
////                android.Manifest.permission.CAMERA,
////                android.Manifest.permission.RECORD_AUDIO
////            ).apply {
////
////                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
////                    add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
////                }
////            }.toTypedArray()
////
////    }
//
//
//}
//
///**
// * ImageAnalyzer
// */
////private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {
////    private fun ByteBuffer.toByteArray(): ByteArray {
////        rewind() // Rewind the buffer to zero
////        val data = ByteArray(remaining())
////        get(data) // copy the buffer into a byte array
////        return data
////    }
////
////    override fun analyze(image: ImageProxy) {
////        val buffer = image.planes[0].buffer
////        val data = buffer.toByteArray()
////        val pixels = data.map { it.toInt() and 0xFF }
////        val luma = pixels.average()
////
////        listener(luma)
////
////        image.close()
////    }
//}
