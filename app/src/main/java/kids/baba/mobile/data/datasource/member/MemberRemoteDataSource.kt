package kids.baba.mobile.data.datasource.member

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.model.SignUpRequest
import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.Flow

interface MemberRemoteDataSource {
    suspend fun getMe(accessToken: String): Flow<MemberModel>

    suspend fun signUp(signToken: String, signUpRequest: SignUpRequest): Flow<TokenResponse>
}
