package kids.baba.mobile.data.datasource.album

import android.util.Log
import kids.baba.mobile.data.api.AlbumApi
import kids.baba.mobile.domain.model.AlbumResponse
import kids.baba.mobile.domain.model.Article
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
        token: String
    ): Flow<AlbumResponse> = flow {
        val response = api.getAlbum(token, id, year, month)
        Log.e("album","${response.code()}")
        response.body()?.let {
            Log.e("album","$it")
            emit(it)
        }
    }

    override suspend fun postArticle(id: String, article: Article) {
        api.addArticle(id, article)
    }
}