package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.BabiesInfoResponse
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface BabyRepository {
    suspend fun getBaby() : Result<BabyResponse>
    suspend fun getBabiesInfo(inviteCode: String): Flow<BabiesInfoResponse>
}
