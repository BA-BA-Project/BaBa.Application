package kids.baba.mobile.data.datasource.album

import kids.baba.mobile.data.api.AlbumApi
import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.Baby
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlbumRemoteDataSourceImpl @Inject constructor(
    private val api: AlbumApi
) : AlbumRemoteDataSource {
    override suspend fun getAlbum(id: Int) = flow {
        emit(api.getAlbum(id))
    }

    override suspend fun getBaby() = flow {
        emit(api.getBaby())
    }
}