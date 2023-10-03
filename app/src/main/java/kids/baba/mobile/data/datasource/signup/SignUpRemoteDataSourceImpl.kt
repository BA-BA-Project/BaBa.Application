package kids.baba.mobile.data.datasource.signup

import kids.baba.mobile.data.api.SignUpApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import javax.inject.Inject

class SignUpRemoteDataSourceImpl @Inject constructor(
    private val signUpApi: SignUpApi,
    private val safeApiHelper: SafeApiHelper
) : SignUpRemoteDataSource {
    override suspend fun signUpWithBabiesInfo(
        signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo
    ) = safeApiHelper.getSafe(
        remoteFetch = { signUpApi.signUpWithBabiesInfo(signUpRequestWithBabiesInfo) },
        mapping = { it }
    )

    override suspend fun signUpWithInviteCode(
        signUpRequestWithInviteCode: SignUpRequestWithInviteCode
    ) = safeApiHelper.getSafe(
        remoteFetch = { signUpApi.signUpWithInviteCode(signUpRequestWithInviteCode) },
        mapping = { it }
    )

}