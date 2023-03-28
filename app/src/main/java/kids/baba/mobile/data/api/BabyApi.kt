package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.BabiesInfoResponse
import kids.baba.mobile.domain.model.BabyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BabyApi {
    @GET("baby/invite-code")
    fun getBabiesInfoByInviteCode(
        @Header("signToken")
        signToken: String,
        @Query("inviteCode")
        inviteCode: String
    ): Response<BabiesInfoResponse>

    //아기 리스트 가져오기
    @GET("baby")
    suspend fun getBaby(): BabyResponse
}