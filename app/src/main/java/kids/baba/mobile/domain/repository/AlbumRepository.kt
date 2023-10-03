package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AlbumRepository {
    suspend fun getAlbum(id: String, year: Int, month: Int): ApiResult<List<Album>>

    suspend fun getOneAlbum(babyId: String, contentId: Int): ApiResult<Album>

    suspend fun postAlbum(
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ): ApiResult<PostAlbumResponse>

    suspend fun deleteAlbum(
        babyId: String,
        contentId: Int
    ): ApiResult<Unit>

    suspend fun likeAlbum(id: String, contentId: Int): ApiResult<Boolean>

    suspend fun addComment(id: String, contentId: Int, commentInput: CommentInput): ApiResult<Unit>

    suspend fun deleteComment(id: String, contentId: Int, commentId: String): ApiResult<Unit>

    suspend fun getComment(id: String, contentId: Int): ApiResult<List<Comment>>

    suspend fun getLikeDetail(id: String, contentId: Int): ApiResult<LikeDetailResponse>

    suspend fun getAllAlbum(id: String): ApiResult<List<Album>>

}