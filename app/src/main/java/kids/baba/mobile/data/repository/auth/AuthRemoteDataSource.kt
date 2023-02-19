package kids.baba.mobile.data.repository.auth

import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    suspend fun login(socialToken: String): Flow<TokenResponse>
}
