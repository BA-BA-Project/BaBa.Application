package kids.baba.mobile.presentation.helper

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kids.baba.mobile.R

class StoragePermissionRequester(
    fragment: Fragment,
    onGranted: () -> Unit,
    onDismissed: () -> Unit,
) : BasePermissionRequester(fragment, onGranted, onDismissed) {
    override val permissions: Array<String>
        get() = arrayOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO
//            ,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    override val titleResId: Int
        get() = R.string.android_permission_camera_title

    override val descriptionResId: Int
        get() = R.string.android_permission_camera_description

    override val descriptionWhenDeniedResId: Int
        get() = R.string.android_permission_camera_denied_description


}