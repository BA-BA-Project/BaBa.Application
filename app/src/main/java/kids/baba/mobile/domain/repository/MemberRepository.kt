package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.MemberModel
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    suspend fun getMe(accessToken: String): Flow<MemberModel>
}
