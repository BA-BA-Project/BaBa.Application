package kids.baba.mobile.data.datasource.album

import kids.baba.mobile.domain.model.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AlbumRemoteDataSource {
    suspend fun getAlbum(id: String, year: Int, month: Int): Result<List<Album>>

    suspend fun postAlbum(
        accessToken: String,
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ): Flow<PostAlbumResponse>

    suspend fun likeAlbum(id: String, contentId: String): Result<Boolean>

    suspend fun addComment(id: String, contentId: String, commentInput: CommentInput): Result<Unit>

    suspend fun deleteComment(id: String, contentId: String, commentId: String) : Result<Unit>

    suspend fun getComment(id: String, contentId: String): Result<List<Comment>>

    suspend fun getLikeDetail(id: String, contentId: String): Result<LikeDetailResponse>
}