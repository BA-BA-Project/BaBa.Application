package kids.baba.mobile.data.datasource.baby

import kids.baba.mobile.data.api.BabyApi
import kids.baba.mobile.domain.model.BabiesInfoResponse
import javax.inject.Inject

class BabyRemoteDataSourceImpl @Inject constructor(
    private val babyApi: BabyApi
) : BabyRemoteDataSource {
    override suspend fun getBabiesInfo(signToken: String, inviteCode: String) : BabiesInfoResponse {
        val resp = babyApi.getBabiesInfoByInviteCode(signToken, inviteCode)

        when (resp.code()) {
            200 -> {
                return resp.body() ?: throw Throwable("data is null")
            }

            else -> throw Throwable("초대 코드 정보 확인 에러")
        }
    }
}