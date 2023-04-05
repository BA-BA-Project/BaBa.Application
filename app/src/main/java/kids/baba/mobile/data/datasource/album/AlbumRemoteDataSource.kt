package kids.baba.mobile.data.datasource.album

import kids.baba.mobile.domain.model.AlbumResponse
import kids.baba.mobile.domain.model.PostAlbumResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AlbumRemoteDataSource {
    suspend fun getAlbum(id: Int): Flow<AlbumResponse>


    suspend fun postAlbum(
        accessToken: String,
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ): Flow<PostAlbumResponse>
}