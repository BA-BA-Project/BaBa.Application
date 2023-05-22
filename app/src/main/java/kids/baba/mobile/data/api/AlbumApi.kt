package kids.baba.mobile.data.api

import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AlbumApi {


    //성장 앨범 메인
    @GET("baby/{babyId}/album")
    suspend fun getAlbum(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("babyId") id: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<AlbumResponse>

    @GET("baby/{babyId}/album/{albumId}")
    suspend fun gatOneAlbum(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("babyId") babyId: String,
        @Path("contentId") contentId: String
    ): Response<Album>

    //성장 앨범 추가
    @Multipart
    @POST("baby/{babyId}/album")
    suspend fun postAlbum(
        @Header("Authorization")
        accessToken: String,
        @Path("babyId") id: String,
        @Part photo: MultipartBody.Part,
        @PartMap bodyDataHashMap: HashMap<String, RequestBody>
    ): Response<PostAlbumResponse>

    @POST("baby/{babyId}/album/{contentId}/like")
    suspend fun likeAlbum(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("babyId") id: String,
        @Path("contentId") contentId: String
    ): Response<LikeResponse>

    @POST("baby/{babyId}/album/{contentId}/comment")
    suspend fun addComment(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("babyId") id: String,
        @Path("contentId") contentId: String,
        @Body commentInput: CommentInput
    ): Response<Unit>

    @DELETE("baby/{babyId}/album/{contentId}/comment/{commentId}")
    suspend fun deleteComment(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("babyId") id: String,
        @Path("contentId") contentId: String,
        @Path("commentId") commentId: String
    ): Response<Unit>

    @GET("baby/{babyId}/album/{contentId}/comments")
    suspend fun getComments(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("contentId") contentId: String,
        @Path("babyId") id: String
    ): Response<CommentResponse>

    @GET("baby/{babyId}/album/{contentId}/likes")
    suspend fun getLikeDetail(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("contentId") contentId: String,
        @Path("babyId") id: String
    ): Response<LikeDetailResponse>

    @GET("baby/{babyId}/album/all")
    suspend fun getAllAlbum(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("babyId") id: String
    ): Response<AlbumResponse>

    //성장 앨범 좋아요

    //성장 앨범 댓글 추가

    //성장 앨범 자세히 보기

    //성장 앨범 사진 수정

    //성장 앨범 카드 수정

    //성장 앨범 댓글 삭제

    //성장 앨범 날짜별 보기
}