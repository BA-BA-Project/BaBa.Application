package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.LoginRequest
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.SignTokenResponse
import kids.baba.mobile.domain.model.TermsResponse
import kids.baba.mobile.domain.model.TokenRefreshRequest
import kids.baba.mobile.domain.model.TokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<TokenResponse>

    @POST("auth/refresh")
    fun tokenRefresh(@Body tokenRefreshRequest: TokenRefreshRequest): Call<TokenResponse>

    @POST("auth/terms")
    suspend fun getTerms(@Body socialToken: String): Response<TermsResponse>

    @POST("auth/login/terms")
    suspend fun getSignToken(@Body signTokenRequest: SignTokenRequest): Response<SignTokenResponse>

}
