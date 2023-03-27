package kids.baba.mobile.domain.usecase

import android.content.Context
import android.content.res.Resources
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kids.baba.mobile.R
import kids.baba.mobile.domain.repository.PhotoPickerRepository
import kids.baba.mobile.domain.model.MediaData
import kotlinx.coroutines.flow.flow
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PhotoPickerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resources: Resources
) : PhotoPickerRepository {

    private val TAG = "GetPhotoFromPickerUseCase"

    override suspend fun getPhoto(uri: Uri) = flow {
//        val imageFile = File(getRealPathFromUri(uri))
//        val imageFile = File(uri.path.toString())
//        val intf: androidx.exifinterface.media.ExifInterface?
//        try {
//            intf = androidx.exifinterface.media.ExifInterface(imageFile)
//            val dateString = intf.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
//            val date = if (dateString != null) {
//                "${dateString.substring(2, 4)}.${dateString.substring(5, 7)}.${dateString.substring(8, 10)}"
//            } else {
//                getStringResource(R.string.can_not_find_date)
//            }
        val date = getRealPathFromUri(uri)

        Log.d(TAG, "Dated : $date") // Display dateString. You can do/use it your own way
        val mediaData = MediaData(
            mediaName = /*uri.name*/uri.toString(),
            mediaPath = uri.toString(),
            mediaDate = date
        )
        emit(mediaData)
//    } catch (e: Exception)
//    {
//        throw e
//    }

    }


    private fun getRealPathFromUri(contentUri: Uri): String {

//        val cursor = context.contentResolver.query(contentUri, null, null, null, null)
//
//        val result: String
//        if (cursor == null) {
//            result = contentUri.path.toString()
//        } else {
//            cursor.moveToFirst()
//            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
//            result = cursor.getString(idx)
//            Log.d(TAG, "result in getRealPathFromUri: $result")
//            cursor.close()
//        }
        context.contentResolver.query(contentUri, null, null, null, null)?.use { c ->
            val dateMod = try {
                val colDateModified = c
                    .getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_LAST_MODIFIED)
                c.moveToFirst()
                c.getLong(colDateModified)
            } catch (e: Exception) {
                0
            }
            Log.e(TAG, " date Long : $dateMod")
            val t_dateFormat = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
            val t_date = Date(dateMod)
            val str_date = t_dateFormat.format(t_date)
            c.close()
            return str_date
//            return t_date.toString()
//            return dateMod.toString()
        }
//        return result
        return getStringResource(R.string.can_not_find_date)
    }

    private fun getStringResource(resourceId: Int): String {
        return resources.getString(resourceId)
    }

}

/*
contentResolver.query( uri, null, null, null, null  )?.use { c ->

    val dateMod: Long = try {
        val colDateModified = c
            .getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_LAST_MODIFIED)
        c.moveToFirst()
        c.getLong(colDateModified)
    } catch (e: Exception) {
        0
    }
    print("Date modified : $dateMod")
}


///

val photoUri: Uri = Uri.withAppendedPath(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        cursor.getString(idColumnIndex)
)

// Get location data using the Exifinterface library.
// Exception occurs if ACCESS_MEDIA_LOCATION permission isn't granted.
photoUri = MediaStore.setRequireOriginal(photoUri)
contentResolver.openInputStream(photoUri)?.use { stream ->
    ExifInterface(stream).run {
        // If lat/long is null, fall back to the coordinates (0, 0).
        val latLong = latLong ?: doubleArrayOf(0.0, 0.0)
    }
}
 */