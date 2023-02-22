package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.LoginRequest
import kids.baba.mobile.domain.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<TokenResponse>
}
