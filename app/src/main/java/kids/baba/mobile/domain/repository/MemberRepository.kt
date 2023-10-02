package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.model.Result

interface MemberRepository {
    suspend fun getMe(accessToken: String): Result<MemberModel>
}
