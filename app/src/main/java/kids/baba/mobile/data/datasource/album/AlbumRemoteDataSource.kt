package kids.baba.mobile.data.datasource.album

import kids.baba.mobile.domain.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AlbumRemoteDataSource {
    suspend fun getAlbum(id: String, year: Int, month: Int): Result<List<Album>>

    suspend fun getOneAlbum(babyId:String, contentId: Int): Result<Album>

    suspend fun postAlbum(
        accessToken: String,
        id: String,
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ): Result<PostAlbumResponse>

    suspend fun likeAlbum(id: String, contentId: Int): Result<Boolean>

    suspend fun addComment(id: String, contentId: Int, commentInput: CommentInput): Result<Unit>

    suspend fun deleteComment(id: String, contentId: Int, commentId: String) : Result<Unit>

    suspend fun getComment(id: String, contentId: Int): Result<List<Comment>>

    suspend fun getLikeDetail(id: String, contentId: Int): Result<LikeDetailResponse>

    suspend fun getAllAlbum(id: String): Result<List<Album>>
}