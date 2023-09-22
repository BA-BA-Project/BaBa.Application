package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.Comment
import kids.baba.mobile.domain.model.CommentInput
import kids.baba.mobile.domain.model.LikeDetailResponse
import kids.baba.mobile.domain.model.PostAlbumResponse
import kids.baba.mobile.domain.model.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AlbumRepository {
    suspend fun getAlbum(id: String, year: Int, month: Int): Result<List<Album>>

    suspend fun getOneAlbum(babyId: String, contentId: Int): Result<Album>

    suspend fun postAlbum(
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ): Result<PostAlbumResponse>

    suspend fun deleteAlbum(
        babyId: String,
        contentId: Int
    ): Result<Unit>

    suspend fun likeAlbum(id: String, contentId: Int): Result<Boolean>

    suspend fun addComment(id: String, contentId: Int, commentInput: CommentInput): Result<Unit>

    suspend fun deleteComment(id: String, contentId: Int, commentId: String) : Result<Unit>

    suspend fun getComment(id: String, contentId: Int): Result<List<Comment>>

    suspend fun getLikeDetail(id: String, contentId: Int): Result<LikeDetailResponse>

    suspend fun getAllAlbum(id: String): Result<List<Album>>

}