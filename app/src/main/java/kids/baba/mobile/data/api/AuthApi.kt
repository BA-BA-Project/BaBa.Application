package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.LoginRequest
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.TermsData
import kids.baba.mobile.domain.model.TokenRefreshRequest
import kids.baba.mobile.domain.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<TokenResponse>

    @POST("api/auth/refresh")
    fun tokenRefresh(@Body tokenRefreshRequest: TokenRefreshRequest): Response<TokenResponse>

    @GET("api/auth/terms")
    suspend fun getTerms(@Body socialToken: String): Response<List<TermsData>>

    @POST("api/auth/login/terms")
    suspend fun getSignToken(@Body signTokenRequest: SignTokenRequest): Response<String>

}
