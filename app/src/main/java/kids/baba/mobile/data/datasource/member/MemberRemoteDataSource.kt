package kids.baba.mobile.data.datasource.member

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.model.Result

interface MemberRemoteDataSource {
    suspend fun getMe(accessToken: String): Result<MemberModel>
}
