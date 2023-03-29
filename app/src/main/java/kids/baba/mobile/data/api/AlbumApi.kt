package kids.baba.mobile.data.api

import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumApi {


    //성장 앨범 메인
    @GET("/album/{babyId}")
    suspend fun getAlbum(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("babyId") id: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<AlbumResponse>

    //성장 앨범 추가
    @POST("/album/{babyId}")
    suspend fun addArticle(@Path("babyId") id: String, @Body article: Article)

    //성장 앨범 좋아요

    //성장 앨범 댓글 추가

    //성장 앨범 자세히 보기

    //성장 앨범 사진 수정

    //성장 앨범 카드 수정

    //성장 앨범 댓글 삭제

    //성장 앨범 날짜별 보기
}