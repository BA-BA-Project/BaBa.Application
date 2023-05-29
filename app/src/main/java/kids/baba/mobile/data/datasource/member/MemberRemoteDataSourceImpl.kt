package kids.baba.mobile.data.datasource.member

import kids.baba.mobile.data.api.MemberApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val memberApi: MemberApi,
    private val safeApiHelper: SafeApiHelper
) : MemberRemoteDataSource {
    override suspend fun getMe(accessToken: String) = safeApiHelper.getSafe(
        remoteFetch = { memberApi.getMe(accessToken) },
        mapping = { it }
    )

    override suspend fun signUpWithBabiesInfo(
        signToken: String,
        signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo,
    ) = safeApiHelper.getSafe(
        remoteFetch = { memberApi.signUpWithBabiesInfo(signToken, signUpRequestWithBabiesInfo) },
        mapping = { it }
    )


    override suspend fun signUpWithInviteCode(
        signToken: String,
        signUpRequestWithInviteCode: SignUpRequestWithInviteCode
    ) = safeApiHelper.getSafe(
        remoteFetch = {memberApi.signUpWithInviteCode(signToken, signUpRequestWithInviteCode)},
        mapping = {it}
    )
}
