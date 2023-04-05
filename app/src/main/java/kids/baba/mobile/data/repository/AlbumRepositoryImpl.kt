package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.album.AlbumRemoteDataSource
import kids.baba.mobile.domain.model.*
import kids.baba.mobile.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(private val dataSource: AlbumRemoteDataSource) :
    AlbumRepository {
    override suspend fun getAlbum(
        id: String,
        year: Int,
        month: Int,
    ): Flow<AlbumResponse> = dataSource.getAlbum(id, year, month)

    override suspend fun addArticle(id: String) =
        dataSource.postArticle(id)


    override suspend fun likeAlbum(id: String, contentId: String): Flow<LikeResponse> =
        dataSource.likeAlbum(id, contentId)

    override suspend fun addComment(id: String, contentId: String, commentInput: CommentInput) {
        dataSource.addComment(id, contentId, commentInput)
    }

    override suspend fun getComment(id: String, contentId: String): Flow<CommentResponse> =
        dataSource.getComment(id = id, contentId = contentId)

    override suspend fun getLikeDetail(id: String, contentId: String): Flow<LikeDetailResponse> =
        dataSource.getLikeDetail(id = id, contentId = contentId)

}