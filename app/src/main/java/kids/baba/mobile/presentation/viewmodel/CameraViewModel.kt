package kids.baba.mobile.presentation.viewmodel

import android.app.Application
import android.content.res.Resources
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MediaData
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val resources: Resources,
    application: Application,
) : BaseViewModel(application) {

    val currentTakenMedia = MutableLiveData<MediaData>()

    internal fun setArgument(args: Any) {
        currentTakenMedia.value = args as MediaData
    }


    private val TAG = "CameraViewModel"

    internal fun savePhoto(path: String,dateInfo : String): MediaData {
        val file = File(path)

        return MediaData(
            mediaName = file.name,
            mediaPath = path,
            mediaDate = dateInfo
        )
    }


    internal fun pickerSavePhoto(uri: Uri): MediaData {
        val imageFile = File(getRealPathFromUri(uri))
        val intf: androidx.exifinterface.media.ExifInterface?
        val date: String
        try {
            intf = androidx.exifinterface.media.ExifInterface(imageFile)
            val dateString = intf.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
            if (dateString != null) {
                date = "${dateString.substring(2, 4)}.${dateString.substring(5, 7)}.${dateString.substring(8, 10)}"
            } else {
                date = getStringResource(R.string.can_not_find_date)
            }

            Log.e(TAG, "Dated : $date") // Display dateString. You can do/use it your own way

            return MediaData(
                mediaName = imageFile.name,
                mediaPath = uri.toString(),
                mediaDate = date
            )

        } catch (e: Exception) {
            throw e
        }
    }

    private fun getRealPathFromUri(contentUri: Uri): String {
        val cursor = context.contentResolver.query(contentUri, null, null, null, null)
        val result: String
        if (cursor == null) {
            result = contentUri.path.toString()
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val dateModified = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)
            Log.e(TAG, "date Modified in the getRealPathFromUri function: $dateModified")
            result = cursor.getString(idx)
            Log.e(TAG, "result in getRealPathFromUri: $result")
            cursor.close()
        }
        return result
    }

    private fun getStringResource(resourceId: Int): String {
        return resources.getString(resourceId)
    }

}

