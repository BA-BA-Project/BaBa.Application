package kids.baba.mobile.data.datasource.file

import kids.baba.mobile.domain.model.Result

interface FileRemoteDataSource {
    suspend fun downloadFile(fileUrl: String): Result<Unit>
}