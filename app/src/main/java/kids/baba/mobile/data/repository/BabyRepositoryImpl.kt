package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.baby.BabyRemoteDataSource
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.repository.BabyRepository
import javax.inject.Inject

class BabyRepositoryImpl @Inject constructor(
    private val babyRemoteDataSource: BabyRemoteDataSource
) : BabyRepository {
    override suspend fun getBaby(): Result<BabyResponse> = babyRemoteDataSource.getBaby()

    override suspend fun getBabiesInfo(inviteCode: String) =
        babyRemoteDataSource.getBabiesInfo(inviteCode)

}