package kids.baba.mobile.presentation.helper

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import kids.baba.mobile.R

abstract class BasePermissionRequester(
    private val fragment: Fragment,
    private val onGranted: () -> Unit,
    protected val onDismissed: () -> Unit,
) {
    protected abstract val permissions: Array<String>

    protected abstract val titleResId: Int
    protected abstract val descriptionResId: Int
    protected abstract val descriptionWhenDeniedResId: Int


    //    private val permissionRequest: ActivityResultLauncher<Array<out String>> =
    private val permissionRequest: ActivityResultLauncher<Array<String>> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants ->
            onPermissionsResult(grants)
        }


    fun checkPermissions(context: Context) {
        val allGranted = permissions.all { p ->
            PermissionChecker.checkSelfPermission(context, p) == PermissionChecker.PERMISSION_GRANTED
        }

        val shouldDisplayExplanation = permissions.any { p -> fragment.shouldShowRequestPermissionRationale(p) }

        if (shouldDisplayExplanation) {
            displayPermissionExplanationDialog(context, then = { permissionRequest.launch(permissions) })
        } else if (!allGranted) {
            permissionRequest.launch(permissions)
        } else {
            onGranted()
        }
    }

    private fun onPermissionsResult(grants: Map<String, Boolean>) {
        if (grants.all { grant -> grant.value }) {
            onGranted()
        } else {
            val userHadPreviouslyDenied = grants.keys.none { p ->
                fragment.shouldShowRequestPermissionRationale(p)
            }

            if (userHadPreviouslyDenied) {
                displayPermissionDeniedDialog(fragment.requireContext())
            } else {
                // User clicked a one time deny, show him the main screen
                onDismissed()
            }
        }
    }

    private fun displayPermissionExplanationDialog(context: Context, then: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(titleResId))
            .setMessage(context.getString(descriptionResId))
            .setPositiveButton("alert ok") { _: DialogInterface?, _: Int -> then() }
            .setCancelable(false)
            .show()
    }

    private fun displayPermissionDeniedDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.android_permission_denied_title))
            .setMessage(context.getString(descriptionWhenDeniedResId))
            .setPositiveButton(R.string.open_setting) {
                    _: DialogInterface?, _: Int ->
                openSettings(context)
            }
            .setNeutralButton(R.string.cancel) { _: DialogInterface?, _: Int -> onDismissed() }
            .setCancelable(false)
            .show()
    }

    protected fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

}