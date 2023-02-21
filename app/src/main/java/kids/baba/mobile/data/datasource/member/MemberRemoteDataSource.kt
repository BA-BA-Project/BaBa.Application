package kids.baba.mobile.data.datasource.member

import kids.baba.mobile.domain.model.MemberModel
import kotlinx.coroutines.flow.Flow

interface MemberRemoteDataSource {
    suspend fun getMe(accessToken: String): Flow<MemberModel>
}
