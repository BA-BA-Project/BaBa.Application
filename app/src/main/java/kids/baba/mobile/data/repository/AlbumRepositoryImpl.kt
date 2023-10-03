package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.album.AlbumRemoteDataSource
import kids.baba.mobile.domain.model.*
import kids.baba.mobile.domain.repository.AlbumRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(private val dataSource: AlbumRemoteDataSource) :
    AlbumRepository {
    override suspend fun getAlbum(
        id: String,
        year: Int,
        month: Int,
    ): ApiResult<List<Album>> = dataSource.getAlbum(id, year, month)

    override suspend fun getOneAlbum(babyId: String, contentId: Int) = dataSource.getOneAlbum(babyId, contentId)

    override suspend fun postAlbum(
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ) = dataSource.postAlbum(id, photo, bodyDataHashMap)

    override suspend fun deleteAlbum(babyId: String, contentId: Int) =
        dataSource.deleteAlbum(babyId, contentId)

    override suspend fun likeAlbum(id: String, contentId: Int): ApiResult<Boolean> =
        dataSource.likeAlbum(id, contentId)

    override suspend fun addComment(id: String, contentId: Int, commentInput: CommentInput): ApiResult<Unit> =
        dataSource.addComment(id, contentId, commentInput)


    override suspend fun deleteComment(id: String, contentId: Int, commentId: String): ApiResult<Unit> =
        dataSource.deleteComment(id = id, contentId = contentId, commentId = commentId)

    override suspend fun getComment(id: String, contentId: Int): ApiResult<List<Comment>> =
        dataSource.getComment(id = id, contentId = contentId)

    override suspend fun getLikeDetail(id: String, contentId: Int): ApiResult<LikeDetailResponse> =
        dataSource.getLikeDetail(id = id, contentId = contentId)

    override suspend fun getAllAlbum(id: String) = dataSource.getAllAlbum(id)


}