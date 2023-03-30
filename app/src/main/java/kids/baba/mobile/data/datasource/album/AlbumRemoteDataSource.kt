package kids.baba.mobile.data.datasource.album

import kids.baba.mobile.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AlbumRemoteDataSource {
    suspend fun getAlbum(id: String, year: Int, month: Int): Flow<AlbumResponse>
    suspend fun postArticle(id: String, article: Article)

    suspend fun likeAlbum(id: String, contentId: String): Flow<LikeResponse>

    suspend fun addComment(id: String, contentId: String, commentInput: CommentInput)

    suspend fun getComment(contentId: String): Flow<CommentResponse>
}