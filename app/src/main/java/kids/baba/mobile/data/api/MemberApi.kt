package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.MemberModel
import retrofit2.Response
import retrofit2.http.GET

interface MemberApi {
    @GET("members")
    suspend fun getMe(): Response<MemberModel>
}
