package kids.baba.mobile.data.datasource.baby

import kids.baba.mobile.core.error.BadRequest
import kids.baba.mobile.data.api.BabyApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BabyRemoteDataSourceImpl @Inject constructor(
    private val babyApi: BabyApi
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

    override suspend fun getBaby() = flow {
        val response = babyApi.getBaby()
        response.body()?.let {
            emit(it)
        }
    }
}