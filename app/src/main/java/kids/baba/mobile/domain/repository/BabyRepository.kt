package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.model.BabiesInfoResponse
import kids.baba.mobile.domain.model.BabyResponse

interface BabyRepository {
    suspend fun getBaby(): ApiResult<BabyResponse>
    suspend fun getBabiesInfo(inviteCode: String): ApiResult<BabiesInfoResponse>
}
