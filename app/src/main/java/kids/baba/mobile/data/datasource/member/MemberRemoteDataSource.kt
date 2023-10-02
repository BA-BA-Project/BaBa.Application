package kids.baba.mobile.data.datasource.member

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.model.ApiResult

interface MemberRemoteDataSource {
    suspend fun getMe(accessToken: String): ApiResult<MemberModel>

}
