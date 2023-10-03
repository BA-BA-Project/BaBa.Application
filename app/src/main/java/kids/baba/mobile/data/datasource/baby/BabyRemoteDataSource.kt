package kids.baba.mobile.data.datasource.baby

import kids.baba.mobile.domain.model.BabiesInfoResponse
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.domain.model.ApiResult

interface BabyRemoteDataSource {
    suspend fun getBabiesInfo(inviteCode: String): ApiResult<BabiesInfoResponse>
    suspend fun getBaby(): ApiResult<BabyResponse>
}