package kids.baba.mobile.data.datasource.album

import android.util.Log
import kids.baba.mobile.core.error.EntityTooLargeException
import kids.baba.mobile.data.api.AlbumApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AlbumRemoteDataSourceImpl @Inject constructor(
    private val api: AlbumApi,
    private val safeApiHelper: SafeApiHelper
) : AlbumRemoteDataSource {

    override suspend fun getAlbum(
        id: String,
        year: Int,
        month: Int,
    ): Flow<AlbumResponse> = flow {
        val response = api.getAlbum(id = id, year = year, month = month)
        Log.e("Album", "${response.body()?.album} ${response.code()}")
        response.body()?.let {
            emit(it)
        }
    }

    override suspend fun postAlbum(
        accessToken: String,
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ): Result<PostAlbumResponse> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.postAlbum(accessToken, id, photo, bodyDataHashMap)
            },
            mapping = {
                it
            }
        )
        return if (result is Result.Failure) {
            if (result.code == 413) {
                Result.Failure(result.code, result.message, EntityTooLargeException())
            } else {
                result
            }
        } else {
            result
        }
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

    override suspend fun getComment(id: String, contentId: String): Flow<CommentResponse> = flow {
        val response = api.getComments(id = id, contentId = contentId)
        response.body()?.let {
            emit(it)
        }
    }

    override suspend fun getLikeDetail(id: String, contentId: String): Flow<LikeDetailResponse> =
        flow {
            val response = api.getLikeDetail(contentId = contentId, id = id)
            response.body()?.let {
                emit(it)
            }
        }

}