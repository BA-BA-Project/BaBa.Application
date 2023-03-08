package kids.baba.mobile.presentation.helper

import android.Manifest.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity

class FragmentPermissionHelper {


    fun startPermissionHelper(
        fr: FragmentActivity,
        fragmentPermissionHelperInterface: FragmentPermissionHelperInterface,
        msg: String
    ) {
        val requestPermissionLauncher =
            fr.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }
        requestPermissionLauncher.launch(permission.CAMERA)

    }


}