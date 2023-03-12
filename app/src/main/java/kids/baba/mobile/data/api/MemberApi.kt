package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.model.SignUpRequest
import kids.baba.mobile.domain.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MemberApi {
    @GET("/api/members")
    suspend fun getMe(@Header("accessToken") accessToken: String): Response<MemberModel>

    @POST("/api/members/baby")
    suspend fun signUp(
        @Header("signToken")
        signToken: String,
        @Body
        signupRequest: SignUpRequest
    ): Response<TokenResponse>

}
