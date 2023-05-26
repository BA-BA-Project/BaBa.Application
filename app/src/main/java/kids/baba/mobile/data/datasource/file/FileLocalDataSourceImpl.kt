package kids.baba.mobile.data.datasource.file

import android.content.ContentValues
import android.content.Context
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
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/BaBa/DownLoad")
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

//            val outputStream = FileOutputStream()
//
//            val buffer = ByteArray(4096)
//            var bytesRead: Int
//            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                outputStream.write(buffer, 0, bytesRead)
//            }
//        }
        }
}