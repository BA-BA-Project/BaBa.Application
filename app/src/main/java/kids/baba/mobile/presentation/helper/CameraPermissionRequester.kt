package kids.baba.mobile.presentation.helper

import android.Manifest
import androidx.fragment.app.Fragment
import kids.baba.mobile.R

class CameraPermissionRequester(
    fragment: Fragment,
    onGranted: () -> Unit,
    onDismissed: () -> Unit,
) : BasePermissionRequester(fragment, onGranted, onDismissed) {
    override val permissions: Array<String>
        get() = arrayOf(
            Manifest.permission.CAMERA
        )

    override val titleResId: Int
        get() = R.string.android_permission_camera_title

    override val descriptionResId: Int
        get() = R.string.android_permission_camera_description

    override val descriptionWhenDeniedResId: Int
        get() = R.string.android_permission_camera_denied_description


}