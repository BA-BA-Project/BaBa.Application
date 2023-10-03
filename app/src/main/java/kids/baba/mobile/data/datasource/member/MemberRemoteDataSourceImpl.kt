package kids.baba.mobile.data.datasource.member

import kids.baba.mobile.data.api.MemberApi
import kids.baba.mobile.data.network.SafeApiHelper
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val memberApi: MemberApi,
    private val safeApiHelper: SafeApiHelper
) : MemberRemoteDataSource {
    override suspend fun getMe() = safeApiHelper.getSafe(
        remoteFetch = {
            memberApi.getMe()
        },
        mapping = { it }
    )
}
