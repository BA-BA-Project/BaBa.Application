package kids.baba.mobile.data.datasource.album

import kids.baba.mobile.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AlbumRemoteDataSource {
    suspend fun getAlbum(id: String, year: Int, month: Int): Flow<AlbumResponse>
    suspend fun postArticle(id: String)

    suspend fun likeAlbum(id: String, contentId: String): Flow<LikeResponse>

    suspend fun addComment(id: String, contentId: String, commentInput: CommentInput)

    suspend fun getComment(id: String, contentId: String): Flow<CommentResponse>

    suspend fun getLikeDetail(id: String, contentId: String): Flow<LikeDetailResponse>
}