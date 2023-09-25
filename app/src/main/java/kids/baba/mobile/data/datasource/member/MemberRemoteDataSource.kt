package kids.baba.mobile.data.datasource.member

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.model.TokenResponse

interface MemberRemoteDataSource {
    suspend fun getMe(accessToken: String): ApiResult<MemberModel>

    suspend fun signUpWithBabiesInfo(signToken: String, signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo): ApiResult<TokenResponse>

    suspend fun signUpWithInviteCode(signToken: String, signUpRequestWithInviteCode: SignUpRequestWithInviteCode): ApiResult<TokenResponse>
}
