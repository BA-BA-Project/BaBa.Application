package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.BabiesInfoResponse
import kids.baba.mobile.domain.model.BabyResponse
import kotlinx.coroutines.flow.Flow

interface BabyRepository {
    suspend fun getBaby(token: String) : Flow<BabyResponse>
    suspend fun getBabiesInfo(signToken: String, inviteCode: String): Result<BabiesInfoResponse>
}
