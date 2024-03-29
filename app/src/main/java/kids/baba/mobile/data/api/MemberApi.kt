package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MemberApi {
    @GET("members")
    suspend fun getMe(@Header("Authorization") accessToken: String): Response<MemberModel>

    @POST("members/baby")
    suspend fun signUpWithBabiesInfo(
        @Header("Authorization")
        signToken: String,
        @Body
        signupRequestWithBabiesInfo: SignUpRequestWithBabiesInfo
    ): Response<TokenResponse>

    @POST("members/baby/invite-code")
    suspend fun signUpWithInviteCode(
        @Header("Authorization")
        signToken: String,
        @Body
        signUpRequestWithInviteCode: SignUpRequestWithInviteCode
    ): Response<TokenResponse>
}
