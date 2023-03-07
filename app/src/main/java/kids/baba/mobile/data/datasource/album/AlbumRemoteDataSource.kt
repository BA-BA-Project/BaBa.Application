package kids.baba.mobile.data.datasource.album

import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.Baby
import kotlinx.coroutines.flow.Flow

interface AlbumRemoteDataSource {
    suspend fun getAlbum(id: Int): Flow<Album>

    suspend fun getBaby(): Flow<Baby>
}