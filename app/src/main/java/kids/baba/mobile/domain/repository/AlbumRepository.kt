package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AlbumRepository {
    suspend fun getAlbum(id: String, year: Int, month: Int): Result<List<Album>>

    suspend fun postAlbum(
        accessToken: String,
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ): Flow<PostAlbumResponse>

    suspend fun likeAlbum(id: String, contentId: String): Result<Boolean>

    suspend fun addComment(id: String, contentId: String, commentInput: CommentInput)

    suspend fun getComment(id: String, contentId: String): Result<List<Comment>>

    suspend fun getLikeDetail(id: String, contentId: String): Result<LikeDetailResponse>

}