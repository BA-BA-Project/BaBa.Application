package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.MemberModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface MemberApi {
    @GET("/api/members")
    suspend fun getMe(@Header("accessToken") accessToken: String): Response<MemberModel>
}
