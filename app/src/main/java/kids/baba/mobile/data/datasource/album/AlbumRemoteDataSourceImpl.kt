package kids.baba.mobile.data.datasource.album

import kids.baba.mobile.core.error.EntityTooLargeException
import kids.baba.mobile.data.api.AlbumApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.Comment
import kids.baba.mobile.domain.model.CommentInput
import kids.baba.mobile.domain.model.LikeDetailResponse
import kids.baba.mobile.domain.model.Result
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
    ): Result<List<Album>> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.getAlbum(id = id, year = year, month = month)
            },
            mapping = {
                it.album
            }
        )
        return result
    }


    override suspend fun postAlbum(
        accessToken: String,
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ) = flow {
        runCatching { api.postAlbum(accessToken, id, photo, bodyDataHashMap) }
            .onSuccess { resp ->
                when (resp.code()) {
                    201 -> {
                        val data = resp.body() ?: throw Throwable("data is null")
                        emit(data)
                    }

                    413 -> {
                        throw EntityTooLargeException("사진의 용량이 너무 큽니다.")
                    }
                }
            }
            .onFailure {
                throw it
            }
    }

    override suspend fun likeAlbum(id: String, contentId: String): Result<Boolean> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.likeAlbum(id = id, contentId = contentId)
            },
            mapping = {
                it.isLiked
            }
        )
        return result
    }

    override suspend fun addComment(id: String, contentId: String, commentInput: CommentInput): Result<Unit> =
        safeApiHelper.getSafe(
            remoteFetch = {
                api.addComment(id = id, contentId = contentId, commentInput = commentInput)
            },
            mapping = {}
        )

    override suspend fun deleteComment(id: String, contentId: String, commentId: String): Result<Unit> =
        safeApiHelper.getSafe(
            remoteFetch = {
                api.deleteComment(id = id, contentId = contentId, commentId = commentId)
            },
            mapping = {}
        )
    override suspend fun getComment(id: String, contentId: String): Result<List<Comment>> =
        safeApiHelper.getSafe(
            remoteFetch = {
                api.getComments(id = id, contentId = contentId)
            },
            mapping = {
                it.comments
            }
        )

    override suspend fun getLikeDetail(id: String, contentId: String): Result<LikeDetailResponse> =
        safeApiHelper.getSafe(
            remoteFetch = {
                api.getLikeDetail(id = id, contentId = contentId)
            },
            mapping = { it }
        )

}