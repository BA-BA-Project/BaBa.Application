package kids.baba.mobile.data.datasource.baby

import kids.baba.mobile.domain.model.BabiesInfoResponse

interface BabyRemoteDataSource {
    suspend fun getBabiesInfo(signToken: String, inviteCode: String): BabiesInfoResponse
}