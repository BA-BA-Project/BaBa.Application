package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.AlbumResponse
import kids.baba.mobile.domain.model.Article
import kids.baba.mobile.domain.model.LikeResponse
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    suspend fun getAlbum(id: String, year: Int, month: Int): Flow<AlbumResponse>

    suspend fun addArticle(id: String, article: Article): Flow<Boolean>

    suspend fun likeAlbum(id: String, contentId: String): Flow<LikeResponse>
}