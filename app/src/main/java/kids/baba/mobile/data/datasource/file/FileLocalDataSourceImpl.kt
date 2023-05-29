package kids.baba.mobile.data.datasource.file

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import javax.inject.Inject

class FileLocalDataSourceImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : FileLocalDataSource {
    override suspend fun saveFile(responseBody: ResponseBody, fileName: String) =
        runCatching {
            val inputStream = responseBody.byteStream()
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/BaBa")
                }
            }

            val contentResolver = context.contentResolver

            val uri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            uri
        }
}