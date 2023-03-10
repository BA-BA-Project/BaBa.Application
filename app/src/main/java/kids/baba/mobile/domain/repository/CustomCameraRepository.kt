//package kids.baba.mobile.domain.repository
//
//import android.content.Context
//import android.net.Uri
//import androidx.camera.view.PreviewView
//import androidx.lifecycle.LifecycleOwner
//import kids.baba.mobile.domain.model.SavedImg
//import kotlinx.coroutines.flow.Flow
//
//interface CustomCameraRepository {
//
//    suspend fun captureAndSaveImage(context: Context): Uri
//
//    suspend fun showCameraPreview(
//        previewView: PreviewView,
//    lifecycleOwner: LifecycleOwner
//    )
//
//}