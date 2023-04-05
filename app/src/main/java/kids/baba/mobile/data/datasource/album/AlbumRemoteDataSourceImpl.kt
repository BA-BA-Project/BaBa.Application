package kids.baba.mobile.data.datasource.album

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
        emit(api.postAlbum(accessToken, id, photo, bodyDataHashMap))
    }
}