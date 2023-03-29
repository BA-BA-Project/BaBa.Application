package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.album.AlbumRemoteDataSource
import kids.baba.mobile.domain.model.AlbumResponse
import kids.baba.mobile.domain.model.Article
import kids.baba.mobile.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(private val dataSource: AlbumRemoteDataSource) :
    AlbumRepository {
    override suspend fun getAlbum(
        id: String,
        year: Int,
        month: Int,
        token: String
    ): Flow<AlbumResponse> = dataSource.getAlbum(id, year, month, token)

    override suspend fun addArticle(id: String, article: Article) {
        dataSource.postArticle(id, article)
    }
}