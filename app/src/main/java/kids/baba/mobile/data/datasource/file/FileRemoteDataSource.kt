package kids.baba.mobile.data.datasource.file

import kids.baba.mobile.domain.model.Result
import okhttp3.ResponseBody

interface FileRemoteDataSource {
    suspend fun downloadFile(fileUrl: String): Result<ResponseBody>
}