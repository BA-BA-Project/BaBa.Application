package kids.baba.mobile.data.datasource.file

import android.net.Uri
import okhttp3.ResponseBody

interface FileLocalDataSource {
    suspend fun saveFile(responseBody: ResponseBody, fileName: String): Result<Uri?>
}