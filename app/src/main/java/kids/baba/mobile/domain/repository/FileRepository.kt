package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.Result

interface FileRepository {
    suspend fun saveFile(fileUrl: String, fileName: String) : Result<Unit>
}