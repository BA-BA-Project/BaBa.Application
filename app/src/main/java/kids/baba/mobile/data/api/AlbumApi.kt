package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.Baby
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AlbumApi {

    //아기 리스트 가져오기
    @GET("/baby")
    suspend fun getBaby(): Baby

    //성장 앨범 메인
    @GET("/album/{babyId}")
    suspend fun getAlbum(@Path("babyId") id: Int): Album

    //성장 앨범 추가
    @POST("/album/{babyId}")
    suspend fun addAlbum(@Path("babyId") id: Int, @Body album: Album) : Album

    //성장 앨범 좋아요

    //성장 앨범 댓글 추가

    //성장 앨범 자세히 보기

    //성장 앨범 사진 수정

    //성장 앨범 카드 수정

    //성장 앨범 댓글 삭제

    //성장 앨범 날짜별 보기
}