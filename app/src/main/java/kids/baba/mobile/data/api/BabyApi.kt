package kids.baba.mobile.data.api

import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.BabiesInfoResponse
import kids.baba.mobile.domain.model.BabyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BabyApi {
    @GET("baby/invitation")
    suspend fun getBabiesInfoByInviteCode(
        @Query("code")
        inviteCode: String
    ): Response<BabiesInfoResponse>

    //아기 리스트 가져오기
    @GET("baby")
    suspend fun getBaby(@Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY)): Response<BabyResponse>
}