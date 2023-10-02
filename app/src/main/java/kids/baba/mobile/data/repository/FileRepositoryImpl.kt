package kids.baba.mobile.data.repository

import android.net.Uri
import kids.baba.mobile.data.datasource.file.FileLocalDataSource
import kids.baba.mobile.data.datasource.file.FileRemoteDataSource
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.repository.FileRepository
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileRemoteDataSource: FileRemoteDataSource,
    private val fileLocalDataSource: FileLocalDataSource
) : FileRepository {
    override suspend fun saveFile(fileUrl: String, fileName: String): ApiResult<Uri> {

        return when (val remoteResult = fileRemoteDataSource.downloadFile(fileUrl)) {
            is ApiResult.Success -> {
                val localResult = fileLocalDataSource.saveFile(remoteResult.data, fileName)
                val uri = localResult.getOrNull()
                if (uri == null) {
                    ApiResult.Unexpected(localResult.exceptionOrNull() ?: Throwable())
                } else {
                    ApiResult.Success(uri)
                }

            }

            is ApiResult.NetworkError -> ApiResult.NetworkError(remoteResult.throwable)
            is ApiResult.Unexpected -> ApiResult.Unexpected(remoteResult.throwable)
            is ApiResult.Failure -> ApiResult.Failure(
                remoteResult.code,
                remoteResult.message,
                remoteResult.throwable
            )
        }

    }
}