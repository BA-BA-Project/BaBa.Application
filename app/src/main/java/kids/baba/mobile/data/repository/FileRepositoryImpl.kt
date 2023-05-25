package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.file.FileRemoteDataSource
import kids.baba.mobile.domain.repository.FileRepository
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileRemoteDataSource: FileRemoteDataSource
) : FileRepository {
    override suspend fun downloadFile(fileUrl: String) = fileRemoteDataSource.downloadFile(fileUrl)
}