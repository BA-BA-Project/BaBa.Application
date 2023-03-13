package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.BabiesInfoResponse

interface BabyRepository {
    suspend fun getBabiesInfo(signToken: String, inviteCode: String): Result<BabiesInfoResponse>
}