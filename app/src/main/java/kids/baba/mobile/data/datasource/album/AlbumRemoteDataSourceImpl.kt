package kids.baba.mobile.data.datasource.album

import kids.baba.mobile.core.error.EntityTooLargeException
import kids.baba.mobile.data.api.AlbumApi
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AlbumRemoteDataSourceImpl @Inject constructor(
    private val api: AlbumApi
) : AlbumRemoteDataSource {
    override suspend fun getAlbum(id: Int) = flow {
        emit(api.getAlbum(id))
    }

    override suspend fun postAlbum(
        accessToken: String,
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ) = flow {
        runCatching { api.postAlbum(accessToken, id, photo, bodyDataHashMap) }
            .onSuccess { resp ->
                when (resp.code()) {
                    201 -> {
                        val data = resp.body() ?: throw Throwable("data is null")
                        emit(data)
                    }
                    413 -> {
                        throw EntityTooLargeException("사진의 용량이 너무 큽니다.")
                    }
                }
            }
            .onFailure {
                throw it
            }
    }
}