package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.model.MemberModel

interface MemberRepository {
    suspend fun getMe(accessToken: String): ApiResult<MemberModel>
}
