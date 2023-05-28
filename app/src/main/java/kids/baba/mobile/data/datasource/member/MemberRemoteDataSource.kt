package kids.baba.mobile.data.datasource.member

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.Flow

interface MemberRemoteDataSource {
    suspend fun getMe(accessToken: String): Result<MemberModel>

    fun signUpWithBabiesInfo(signToken: String, signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo): Flow<TokenResponse>

    fun signUpWithInviteCode(signToken: String, signUpRequestWithInviteCode: SignUpRequestWithInviteCode): Flow<TokenResponse>
}
