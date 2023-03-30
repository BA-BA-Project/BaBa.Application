package kids.baba.mobile.data.datasource.album

import android.util.Log
import kids.baba.mobile.data.api.AlbumApi
import kids.baba.mobile.domain.model.AlbumResponse
import kids.baba.mobile.domain.model.Article
import kids.baba.mobile.domain.model.LikeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlbumRemoteDataSourceImpl @Inject constructor(
    private val api: AlbumApi
) : AlbumRemoteDataSource {

    override suspend fun getAlbum(
        id: String,
        year: Int,
        month: Int,
    ): Flow<AlbumResponse> = flow {
        val response = api.getAlbum(id = id, year = year, month = month)
        response.body()?.let {
            emit(it)
        }
    }

    override suspend fun postArticle(id: String, article: Article) {
        api.addArticle(article = article, id = id)
    }

    override suspend fun likeAlbum(id: String, contentId: String): Flow<LikeResponse> = flow {
        val response = api.likeAlbum(id = id, contentId = contentId)
        response.body()?.let {
            emit(it)
        }
    }
}