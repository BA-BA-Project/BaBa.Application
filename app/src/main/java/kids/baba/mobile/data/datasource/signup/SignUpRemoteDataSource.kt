package kids.baba.mobile.data.datasource.signup

import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.model.TokenResponse

interface SignUpRemoteDataSource {
    suspend fun signUpWithBabiesInfo(
        signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo
    ): ApiResult<TokenResponse>

    suspend fun signUpWithInviteCode(
        signUpRequestWithInviteCode: SignUpRequestWithInviteCode
    ): ApiResult<TokenResponse>
}