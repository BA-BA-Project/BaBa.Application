package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.baby.BabyRemoteDataSource
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.domain.repository.BabyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BabyRepositoryImpl @Inject constructor(
    private val babyRemoteDataSource: BabyRemoteDataSource
    ) :
    BabyRepository {
    override suspend fun getBaby(): Flow<BabyResponse> = babyRemoteDataSource.getBaby()

    override suspend fun getBabiesInfo(
        signToken: String,
        inviteCode: String
    ) = runCatching {
        babyRemoteDataSource.getBabiesInfo(signToken, inviteCode)
    }
}