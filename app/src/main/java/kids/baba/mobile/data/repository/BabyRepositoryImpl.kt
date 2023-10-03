package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.baby.BabyRemoteDataSource
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.domain.repository.BabyRepository
import javax.inject.Inject

class BabyRepositoryImpl @Inject constructor(
    private val babyRemoteDataSource: BabyRemoteDataSource
) : BabyRepository {
    override suspend fun getBaby(): ApiResult<BabyResponse> = babyRemoteDataSource.getBaby()

    override suspend fun getBabiesInfo(inviteCode: String) =
        babyRemoteDataSource.getBabiesInfo(inviteCode)

}