package kids.baba.mobile.domain.usecase

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.domain.repository.PhotoPickerRepository
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PhotoPickerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resources: Resources
) : PhotoPickerRepository {

    private val TAG = "GetPhotoFromPickerUseCase"

    override suspend fun getPhoto(uri: Uri) = flow {

        val date = getRealPathFromUri(uri)

        val mediaData = MediaData(
            mediaName = /*uri.name*/uri.toString(),
            mediaPath = uri.toString(),
            mediaDate = date
        )
        emit(mediaData)

    }


    private fun getRealPathFromUri(contentUri: Uri): String {

        context.contentResolver.query(contentUri, null, null, null, null)?.use { c ->
            val dateMod = try {
                val colDateModified =
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                        c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)
                    } else {
                        c.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_LAST_MODIFIED)
                    }
                c.moveToFirst()
                c.getLong(colDateModified)
            } catch (e: Exception) {
                throw e
            }
            val result = SimpleDateFormat("yy.MM.dd", Locale.KOREA).format(dateMod)
            Log.d(TAG, "DATE RESULT $result")
            c.close()
            return result
        }

        return getStringResource(R.string.can_not_find_date)
    }

    private fun getStringResource(resourceId: Int): String {
        return resources.getString(resourceId)
    }

}

