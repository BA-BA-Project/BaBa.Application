package kids.baba.mobile.data.repository

import android.net.Uri
import kids.baba.mobile.data.datasource.file.FileLocalDataSource
import kids.baba.mobile.data.datasource.file.FileRemoteDataSource
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.repository.FileRepository
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileRemoteDataSource: FileRemoteDataSource,
    private val fileLocalDataSource: FileLocalDataSource
) : FileRepository {
    override suspend fun saveFile(fileUrl: String, fileName: String): Result<Uri> {

        return when (val remoteResult = fileRemoteDataSource.downloadFile(fileUrl)) {
            is Result.Success -> {
                val localResult = fileLocalDataSource.saveFile(remoteResult.data, fileName)
                val uri = localResult.getOrNull()
                if (uri == null) {
                    Result.Unexpected(localResult.exceptionOrNull() ?: Throwable())
                } else {
                    Result.Success(uri)
                }

            }

            is Result.NetworkError -> Result.NetworkError(remoteResult.throwable)
            is Result.Unexpected -> Result.Unexpected(remoteResult.throwable)
            is Result.Failure -> Result.Failure(
                remoteResult.code,
                remoteResult.message,
                remoteResult.throwable
            )
        }

    }
}