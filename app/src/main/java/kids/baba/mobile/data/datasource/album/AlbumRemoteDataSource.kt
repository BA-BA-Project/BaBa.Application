package kids.baba.mobile.data.datasource.album

import kids.baba.mobile.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AlbumRemoteDataSource {
    suspend fun getAlbum(id: Int): Flow<AlbumResponse>

    suspend fun getBaby(): Flow<BabyResponse>

    suspend fun postArticle(id: Int, article: Article)
}