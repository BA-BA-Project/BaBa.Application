package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AlbumApi {

    //성장 앨범 메인
    @GET("/album/{babyId}")
    suspend fun getAlbum(@Path("babyId") id: Int): AlbumResponse

    //성장 앨범 추가
    @Multipart
    @POST("baby/{babyId}/album")
    suspend fun postAlbum(
        @Header("Authorization")
        accessToken: String,
        @Path("babyId") id: String,
        @Part photo: MultipartBody.Part,
        @PartMap bodyDataHashMap: HashMap<String, RequestBody>
    ): PostAlbumResponse


    //성장 앨범 좋아요

    //성장 앨범 댓글 추가

    //성장 앨범 자세히 보기

    //성장 앨범 사진 수정

    //성장 앨범 카드 수정

    //성장 앨범 댓글 삭제

    //성장 앨범 날짜별 보기
}