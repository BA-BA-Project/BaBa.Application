package kids.baba.mobile.data.datasource.baby

import kids.baba.mobile.data.api.BabyApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.domain.model.Result
import javax.inject.Inject

class BabyRemoteDataSourceImpl @Inject constructor(
    private val babyApi: BabyApi,
    private val safeApiHelper: SafeApiHelper
) : BabyRemoteDataSource {
    override suspend fun getBabiesInfo(inviteCode: String) = safeApiHelper.getSafe(
        remoteFetch = {babyApi.getBabiesInfoByInviteCode(inviteCode)},
        mapping = {it}
    )

    override suspend fun getBaby() : Result<BabyResponse>{
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                babyApi.getBaby()
            },
            mapping = {
                it
            }
        )
        return result
    }
}