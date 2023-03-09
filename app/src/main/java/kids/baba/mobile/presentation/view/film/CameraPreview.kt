package kids.baba.mobile.presentation.view.film

import android.content.Context
import android.hardware.Camera
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kids.baba.mobile.presentation.util.calculatePreviewOrientation

import java.io.IOException

/**
 * Camera preview that displays a [Camera].
 *
 *
 * Handles basic lifecycle methods to display and stop the preview.
 *
 *
 * Implementation is based directly on the documentation at
 * http://developer.android.com/guide/topics/media/camera.html
 */
class CameraPreview @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val camera: Camera? = null,
    private val cameraInfo: Camera.CameraInfo? = null,
    private val displayOrientation: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    private val TAG = "CameraPreview"

    init {

        // Do not initialise if no camera has been set
        if (camera != null && cameraInfo != null) {
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            holder.addCallback(this@CameraPreview)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            camera?.run {
                setPreviewDisplay(holder)
                startPreview()
            }
            Log.d(TAG, "Camera preview started.")
        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: ${e.message}")
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (holder.surface == null) {
            // preview surface does not exist
            Log.d(TAG, "Preview surface does not exist")
            return
        }
        Log.d(TAG, "Preview stopped.")
        camera?.run {
            // stop preview before making changes
            stopPreview()
            cameraInfo?.let {
                setDisplayOrientation(it.calculatePreviewOrientation(displayOrientation))
            }
            setPreviewDisplay(holder)
            startPreview()
            Log.d(TAG, "Camera preview started.")
        }
    }

}
