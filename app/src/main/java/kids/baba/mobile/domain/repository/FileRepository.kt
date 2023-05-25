package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.Result

interface FileRepository {
    suspend fun downloadFile(fileUrl: String) : Result<Unit>
}