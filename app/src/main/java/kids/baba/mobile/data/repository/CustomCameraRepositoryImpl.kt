//package kids.baba.mobile.data.repository
//
//import android.content.ContentValues
//import android.content.Context
//import android.net.Uri
//import android.os.Build
//import android.provider.MediaStore
//import android.widget.Toast
//import androidx.camera.core.*
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.LifecycleOwner
//import kids.baba.mobile.domain.model.SavedImg
//import kids.baba.mobile.domain.repository.CustomCameraRepository
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import java.text.SimpleDateFormat
//import java.util.Locale
//import javax.inject.Inject
//
//class CustomCameraRepositoryImpl @Inject constructor(
//    private val cameraProvider: ProcessCameraProvider,
//    private val selector: CameraSelector,
//    private val preview: Preview,
//    private val imageAnalysis: ImageAnalysis,
//    private val imageCapture: ImageCapture
//) : CustomCameraRepository {
//
//    override suspend fun captureAndSaveImage(context: Context):Uri {
//        // File name
//        val name = SimpleDateFormat(
//            "yyyy-MM-dd-HH-mm-ss-SSS",
//            Locale.KOREA
//        ).format(System.currentTimeMillis())
//
//        // Store in MediaStore
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            if (Build.VERSION.SDK_INT > 28) {
//                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/BaBa-App-Images")
//            }
//        }
//
//        // for capture output
//        val outputOptions = ImageCapture.OutputFileOptions
//            .Builder(
//                context.contentResolver,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                contentValues
//            )
//            .build()
//
//
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(context),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    Toast.makeText(
//                        context, "Saved image ${outputFileResults.savedUri}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//
//
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    Toast.makeText(context, "some error occurred", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        )
//
//    }
//
//
//    override suspend fun showCameraPreview(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
//        preview.setSurfaceProvider(previewView.surfaceProvider)
//        try {
//            cameraProvider.unbindAll()
//            cameraProvider.bindToLifecycle(
//                lifecycleOwner, selector, preview, imageAnalysis, imageCapture
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//}