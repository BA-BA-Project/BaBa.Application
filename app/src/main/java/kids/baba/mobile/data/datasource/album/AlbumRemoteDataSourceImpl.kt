package kids.baba.mobile.data.datasource.album

import android.util.Log
import kids.baba.mobile.data.api.AlbumApi
import kids.baba.mobile.domain.model.*
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
        Log.e("Album","${response.body()?.album} ${response.code()}")
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

    override suspend fun addComment(id: String, contentId: String, commentInput: CommentInput) {
        api.addComment(id = id, contentId = contentId, commentInput = commentInput)
    }

    override suspend fun getComment(contentId: String): Flow<CommentResponse> = flow {
        val response = api.getComments(contentId = contentId)
        response.body()?.let {
            emit(it)
        }
    }

    override suspend fun getLikeDetail(id: String,contentId: String): Flow<LikeDetailResponse> = flow {
        val response = api.getLikeDetail(contentId = contentId, id = id)
        response.body()?.let {
            emit(it)
        }
    }
}