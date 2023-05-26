package kids.baba.mobile.data.datasource.file

import kids.baba.mobile.data.api.FileApi
import kids.baba.mobile.data.network.SafeApiHelper
import javax.inject.Inject

class FileRemoteDataSourceImpl @Inject constructor(
    private val fileApi: FileApi,
    private val safeApiHelper: SafeApiHelper
) : FileRemoteDataSource {
    override suspend fun downloadFile(fileUrl: String) =
        safeApiHelper.getSafe(
            remoteFetch = {
                fileApi.downloadFile(fileUrl)
            },
            mapping = {it}
        )
}