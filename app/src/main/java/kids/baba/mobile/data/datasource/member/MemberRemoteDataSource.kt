package kids.baba.mobile.data.datasource.member

import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.model.MemberModel

interface MemberRemoteDataSource {
    suspend fun getMe(): ApiResult<MemberModel>

}
