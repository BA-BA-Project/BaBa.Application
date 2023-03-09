package kids.baba.mobile.presentation.view

import android.Manifest
import android.app.AlertDialog.Builder
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.presentation.util.*
import kids.baba.mobile.presentation.view.film.CameraPreviewActivity
import kids.baba.mobile.presentation.view.film.FilmDialog

@AndroidEntryPoint
class GrowthAlbumActivity : AppCompatActivity(), FilmDialog.FilmDialogListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private val tag = "GrowthAlbumActivity"
    private lateinit var layout: View
    private lateinit var navController: NavController

    // Register the permissions callback, which handles the user's response to the system permissions dialog. Save the return value, an instance of ActivityResultLauncher.
    // You can use either a val, as shown in this snippet, or a lateinit var in your onAttach() or onCreate() method.
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. goto Camera
//                openCamera()

            } else {
                // Explain to the user that the feature is unavailable because the feature requires a permission that the user has denied.
                // At the same time, respect the user's decision.
                // Don't link to system settings in an effort to convince the user to change their decision.

                // 처음 권한 동의 - 왜 권한이 필요한지 설명하고 그에 대해 동의 구하기
                requestPermissionDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_growh_album)
        layout = findViewById(R.id.activity_growth_album)
        setNavController()
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_growth_album) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onDialogCameraClick(dialog: DialogFragment) {
        Log.d(tag, "CAMERA Click")
        showCameraPreview()
//        when {
//            ContextCompat.checkSelfPermission(
//                this, Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                // use the API that requires the permission
//                openCamera()
//            }
//
//            // 이전에 거부 눌렀을 경우 설정에 직접 들어가서 권한 허용 해야 한다고 말해야 함.
//            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
//                showAlertDialog()
//            }
//
//            else -> {
//                // directly ask for the permission
//                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
//                requestCameraPermission()
//            }
//        }


    }

    override fun onDialogGalleryClick(dialog: DialogFragment) {
        Log.d(tag, "Gallery Click")
    }

    // 카메라 열기
    private fun showCameraPreview() {
        // Check if the Camera permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Log.d(tag, "Permission is already available, start camera preview")

            layout.showSnackbar(R.string.camera_permission_available, Snackbar.LENGTH_SHORT)

            startCamera()
        } else {
            // Permission is missing and must be requested.
            Log.d(tag, "Permission is missing and must be requested.")
            requestCameraPermission()
        }
    }

    private fun showAlertDialog() {
        Log.d(tag, "showAlertDialog - 이전에 거부 눌렀을 경우 설정에 직접 들어가서 권한 허용 해야 한다고 말해야 함.")
    }

    private fun requestPermissionDialog() {

    }

    /**
     * Requests the [android.Manifest.permission.CAMERA] permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private fun requestCameraPermission() {
        // Permission has not been granted and must be requested.
        Log.d(tag,"Permission has not been granted and must be requested.")
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Log.d(tag, "Display a SnackBar with a button to request the missing permission.")
            layout.showSnackbar(R.string.camera_access_required,
                Snackbar.LENGTH_INDEFINITE, R.string.ok) {
                requestPermissionsCompat(arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CAMERA)
            }
            Log.d(tag, "After show snackBar")

        } else {
            Log.d(tag, "camera permission not available")
            layout=findViewById(R.id.activity_growth_album)
            layout.showSnackbar(R.string.camera_permission_not_available, Snackbar.LENGTH_SHORT)

            // Request the permission. The result will be received in onRequestPermissionResult().
            requestPermissionsCompat(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
        }
    }


    private fun startCamera() {
        val intent = Intent(this, CameraPreviewActivity::class.java)
        startActivity(intent)
    }

}
