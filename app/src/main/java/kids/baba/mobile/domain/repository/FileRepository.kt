package kids.baba.mobile.domain.repository

import android.net.Uri
import kids.baba.mobile.domain.model.ApiResult

interface FileRepository {
    suspend fun saveFile(fileUrl: String, fileName: String) : ApiResult<Uri>
}