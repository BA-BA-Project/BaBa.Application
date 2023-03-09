package kids.baba.mobile.presentation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.presentation.view.film.CameraActivity
import kids.baba.mobile.presentation.view.film.FilmDialog

@AndroidEntryPoint
class GrowthAlbumActivity : AppCompatActivity(), FilmDialog.FilmDialogListener,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private val tag = "GrowthAlbumActivity"
    private lateinit var layout: View
    private lateinit var navController: NavController

    private val PERMISSION_REQUEST_CAMERA = 1

    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA)

    // Register the permissions callback, which handles the user's response to the system permissions dialog. Save the return value, an instance of ActivityResultLauncher.
    // You can use either a val, as shown in this snippet, or a lateinit var in your onAttach() or onCreate() method.
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            Log.d("tag", "cameraPermissionGranted : $isGranted")
            if (isGranted) {
                // Permission is granted. goto Camera

                startCamera()

            } else {
                // 처음 권한 동의 - 왜 권한이 필요한지 설명하고 그에 대해 동의 구하기
//                requestCameraPermission()
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
        checkContactsPermission()
    }

    override fun onDialogGalleryClick(dialog: DialogFragment) {
        Log.d(tag, "Gallery Click")
    }


    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
            }
        }

    }

    private fun showRationaleDialog(title: String, message: String, permission: String, requestCode: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { dialog, which ->
                requestPermissions(arrayOf(permission), requestCode)
            }
        builder.create().show()
    }

    private fun checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(tag, "Permission for contacts is not granted")

            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showRationaleDialog(
                    getString(R.string.rationale_title),
                    getString(R.string.rationale_desc),
                    Manifest.permission.CAMERA,
                    PERMISSION_REQUEST_CAMERA
                )
            } else {
                Log.d(tag, "Checking permission")
                requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            }
        } else {
            startCamera()
        }
    }

}
