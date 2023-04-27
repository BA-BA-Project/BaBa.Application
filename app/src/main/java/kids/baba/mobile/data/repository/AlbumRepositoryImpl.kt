package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.album.AlbumRemoteDataSource
import kids.baba.mobile.domain.model.AlbumResponse
import kids.baba.mobile.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(private val dataSource: AlbumRemoteDataSource) :
    AlbumRepository {
    private val tag = "AlbumRepositoryImpl"

    override suspend fun getAlbum(id: Int): Flow<AlbumResponse> = dataSource.getAlbum(id)

    override suspend fun postAlbum(
        accessToken: String,
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ) = flow {
        dataSource.postAlbum(accessToken, id, photo, bodyDataHashMap).catch {
            throw it
        }.collect {
            emit(it)
        }
    }
}