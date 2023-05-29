package kids.baba.mobile.data.datasource.baby

import kids.baba.mobile.domain.model.BabiesInfoResponse
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.domain.model.Result

interface BabyRemoteDataSource {
    suspend fun getBabiesInfo(inviteCode: String): Result<BabiesInfoResponse>
    suspend fun getBaby(): Result<BabyResponse>
}