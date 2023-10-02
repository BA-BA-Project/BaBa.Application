package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.model.TokenResponse

interface SignUpRepository {
    suspend fun signUpWithBabiesInfo(
        signToken: String,
        signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo
    ): Result<TokenResponse>

    suspend fun signUpWithInviteCode(
        signToken: String,
        signUpRequestWithInviteCode: SignUpRequestWithInviteCode
    ): Result<TokenResponse>
}