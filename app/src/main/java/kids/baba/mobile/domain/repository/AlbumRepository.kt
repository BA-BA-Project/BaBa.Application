package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.*
import kids.baba.mobile.domain.model.Result
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
    ): Result<PostAlbumResponse>

    suspend fun likeAlbum(id: String, contentId: String): Result<Boolean>

    suspend fun addComment(id: String, contentId: String, commentInput: CommentInput)

    suspend fun getComment(id: String, contentId: String): Flow<CommentResponse>

    suspend fun getLikeDetail(id: String, contentId: String): Flow<LikeDetailResponse>

    suspend fun getAllAlbum(id: String): Result<List<Album>>

}