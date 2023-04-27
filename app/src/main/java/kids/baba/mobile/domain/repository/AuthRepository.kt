package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.TermsData
import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(socialToken: String): Result<TokenResponse>

    suspend fun getTerms(socialToken: String): Result<List<TermsData>>

    fun getSignToken(signTokenRequest: SignTokenRequest): Flow<String>
}
