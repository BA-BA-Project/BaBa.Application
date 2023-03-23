package kids.baba.mobile.domain.usecase

import android.content.Context
import android.content.res.Resources
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kids.baba.mobile.R
import kids.baba.mobile.domain.repository.PhotoPickerRepository
import kids.baba.mobile.domain.model.MediaData
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class PhotoPickerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resources: Resources
): PhotoPickerRepository {

    private val TAG = "GetPhotoFromPickerUseCase"

    override suspend fun getPhoto(uri: Uri) = flow {
        val imageFile = File(getRealPathFromUri(uri))
        val intf: androidx.exifinterface.media.ExifInterface?
        try {
            intf = androidx.exifinterface.media.ExifInterface(imageFile)
            val dateString = intf.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
            val date = if (dateString != null) {
                "${dateString.substring(2, 4)}.${dateString.substring(5, 7)}.${dateString.substring(8, 10)}"
            } else {
                getStringResource(R.string.can_not_find_date)
            }

            Log.d(TAG, "Dated : $date") // Display dateString. You can do/use it your own way
            val mediaData = MediaData(
                mediaName = imageFile.name,
                mediaPath = uri.toString(),
                mediaDate = date
            )
            emit(mediaData)
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
            result = cursor.getString(idx)
            Log.d(TAG, "result in getRealPathFromUri: $result")
            cursor.close()
        }
        return result
    }
    private fun getStringResource(resourceId: Int): String {
        return resources.getString(resourceId)
    }

}