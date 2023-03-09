package kids.baba.mobile.presentation.view.film

import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kids.baba.mobile.R

private const val TAG = "CameraPreviewActivity"
private const val CAMERA_ID = 0

/**
 * Displays a [CameraPreview] of the first [Camera].
 * An error message is displayed if the Camera is not available.
 *
 *
 * This Activity is only used to illustrate that access to the Camera API has been granted (or
 * denied) as part of the runtime permissions model. It is not relevant for the use of the
 * permissions API.
 *
 *
 * Implementation is based directly on the documentation at
 * http://developer.android.com/guide/topics/media/camera.html
 */
class CameraPreviewActivity : AppCompatActivity() {

    private var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Open an instance of the first camera and retrieve its info.
        camera = getCameraInstance(CAMERA_ID)
        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(CAMERA_ID, cameraInfo)

        if (camera == null) {
            // Camera is not available, display error message.
            setContentView(R.layout.activity_camera_unavailable)
        } else {
            setContentView(R.layout.activity_camera)

            // Get the rotation of the screen to adjust the preview image accordingly.
            val displayRotation = windowManager.defaultDisplay.rotation

            // Create the Preview view and set it as the content of this Activity.
            val cameraPreview = CameraPreview(this, null,
                0, camera, cameraInfo, displayRotation)
            findViewById<FrameLayout>(R.id.camera_preview).addView(cameraPreview)
        }
    }

    public override fun onPause() {
        super.onPause()
        // Stop camera access
        releaseCamera()
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private fun getCameraInstance(cameraId: Int): Camera? {
        var c: Camera? = null
        try {
            c = Camera.open(cameraId) // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "Camera $cameraId is not available: ${e.message}")
        }

        return c // returns null if camera is unavailable
    }

    private fun releaseCamera() {
        camera?.release()
        camera = null
    }
}
