package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.domain.model.BabyResponse
import kotlinx.coroutines.flow.Flow

interface BabyRepository {
    suspend fun getBaby() : Flow<BabyResponse>
}