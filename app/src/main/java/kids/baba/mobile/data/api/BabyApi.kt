package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.BabiesInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BabyApi {
    @GET("api/baby/invite-code")
    fun getBabiesInfoByInviteCode(
        @Header("signToken")
        signToken: String,
        @Query("inviteCode")
        inviteCode: String
    ): Response<BabiesInfoResponse>
}