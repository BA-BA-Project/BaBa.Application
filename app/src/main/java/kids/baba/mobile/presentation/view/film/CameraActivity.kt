package kids.baba.mobile.presentation.view.film

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import kids.baba.mobile.databinding.ActivityCameraBinding

typealias LumaListener = (luma: Double) -> Unit

class CameraActivity : AppCompatActivity() {

    private lateinit var viewBinding:ActivityCameraBinding

    private var imageCaputre: ImageCapture? = null

}
