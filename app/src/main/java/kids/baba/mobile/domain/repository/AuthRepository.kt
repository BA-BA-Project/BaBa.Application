package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(socialToken: String): Flow<TokenResponse>
}
