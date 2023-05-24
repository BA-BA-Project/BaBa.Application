package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.album.AlbumRemoteDataSource
import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.CommentInput
import kids.baba.mobile.domain.model.CommentResponse
import kids.baba.mobile.domain.model.LikeDetailResponse
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(private val dataSource: AlbumRemoteDataSource) :
    AlbumRepository {
    override suspend fun getAlbum(
        id: String,
        year: Int,
        month: Int,
    ): Result<List<Album>> = dataSource.getAlbum(id, year, month)

    override suspend fun postAlbum(
        accessToken: String,
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ) = dataSource.postAlbum(accessToken, id, photo, bodyDataHashMap)

    override suspend fun deleteAlbum(babyId: String, contentId: Int) =
        dataSource.deleteAlbum(babyId, contentId)

    override suspend fun likeAlbum(id: String, contentId: String): Result<Boolean> =
        dataSource.likeAlbum(id, contentId)

    override suspend fun addComment(id: String, contentId: String, commentInput: CommentInput) {
        dataSource.addComment(id, contentId, commentInput)
    }

    override suspend fun getComment(id: String, contentId: String): Flow<CommentResponse> =
        dataSource.getComment(id = id, contentId = contentId)

    override suspend fun getLikeDetail(id: String, contentId: String): Flow<LikeDetailResponse> =
        dataSource.getLikeDetail(id = id, contentId = contentId)

    override suspend fun getAllAlbum(id: String) = dataSource.getAllAlbum(id)


}