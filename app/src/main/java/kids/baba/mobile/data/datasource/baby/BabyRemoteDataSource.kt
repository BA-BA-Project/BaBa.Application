package kids.baba.mobile.data.datasource.baby

import kids.baba.mobile.domain.model.BabiesInfoResponse
import kids.baba.mobile.domain.model.BabyResponse
import kotlinx.coroutines.flow.Flow

interface BabyRemoteDataSource {
    suspend fun getBabiesInfo(signToken: String, inviteCode: String): BabiesInfoResponse
    suspend fun getBaby(): Flow<BabyResponse>
}