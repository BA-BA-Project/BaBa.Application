package kids.baba.mobile.data.datasource.baby

import kids.baba.mobile.core.error.BadRequest
import kids.baba.mobile.data.api.BabyApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.domain.model.Result
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BabyRemoteDataSourceImpl @Inject constructor(
    private val babyApi: BabyApi,
    private val safeApiHelper: SafeApiHelper
) : BabyRemoteDataSource {
    override suspend fun getBabiesInfo(inviteCode: String) = flow {
        runCatching { babyApi.getBabiesInfoByInviteCode(inviteCode) }
            .onSuccess { resp ->
                when (resp.code()) {
                    200 -> {
                        val data = resp.body() ?: throw Throwable("data is null")
                        emit(data)
                    }
                    else -> {
                        throw BadRequest(resp.message())
                    }
                }
            }
            .onFailure {
                throw it
            }
    }

    override suspend fun getBaby() : Result<BabyResponse>{
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                babyApi.getBaby()
            },
            mapping = {
                it
            }
        )
        return result
    }
}