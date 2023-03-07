package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    suspend fun getAlbum(id: Int): Flow<Album>
}