package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AlbumApi {

    //성장 앨범 메인
    @GET("baby/{babyId}/album")
    suspend fun getAlbum(
        @Path("babyId") id: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<AlbumResponse>

    @GET("baby/{babyId}/album/{albumId}")
    suspend fun gatOneAlbum(
        @Path("babyId") babyId: String,
        @Path("contentId") contentId: Int
    ): Response<Album>

    //성장 앨범 추가
    @Multipart
    @POST("baby/{babyId}/album")
    suspend fun postAlbum(
        @Path("babyId") id: String,
        @Part photo: MultipartBody.Part,
        @PartMap bodyDataHashMap: HashMap<String, RequestBody>
    ): Response<PostAlbumResponse>

    @DELETE("baby/{babyId}/album/{contentId}")
    suspend fun deleteAlbum(
        @Path("babyId") babyId: String,
        @Path("contentId") contentId: Int
    ): Response<Unit>

    @POST("baby/{babyId}/album/{contentId}/like")
    suspend fun likeAlbum(
        @Path("babyId") id: String,
        @Path("contentId") contentId: Int
    ): Response<LikeResponse>

    @POST("baby/{babyId}/album/{contentId}/comment")
    suspend fun addComment(
        @Path("babyId") id: String,
        @Path("contentId") contentId: Int,
        @Body commentInput: CommentInput
    ): Response<Unit>

    @DELETE("baby/{babyId}/album/{contentId}/comment/{commentId}")
    suspend fun deleteComment(
        @Path("babyId") id: String,
        @Path("contentId") contentId: Int,
        @Path("commentId") commentId: String
    ): Response<Unit>

    @GET("baby/{babyId}/album/{contentId}/comments")
    suspend fun getComments(
        @Path("contentId") contentId: Int,
        @Path("babyId") id: String
    ): Response<CommentResponse>

    @GET("baby/{babyId}/album/{contentId}/likes")
    suspend fun getLikeDetail(
        @Path("contentId") contentId: Int,
        @Path("babyId") id: String
    ): Response<LikeDetailResponse>

    @GET("baby/{babyId}/album/all")
    suspend fun getAllAlbum(
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