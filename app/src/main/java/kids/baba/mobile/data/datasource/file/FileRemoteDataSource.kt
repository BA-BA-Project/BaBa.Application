package kids.baba.mobile.data.datasource.file

import kids.baba.mobile.domain.model.ApiResult
import okhttp3.ResponseBody

interface FileRemoteDataSource {
    suspend fun downloadFile(fileUrl: String): ApiResult<ResponseBody>
}