package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.TokenResponse
import kids.baba.mobile.presentation.model.TermsUiModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(socialToken: String): Result<TokenResponse>

    fun getTerms(socialToken: String): Flow<List<TermsUiModel>>

    fun getSignToken(signTokenRequest: SignTokenRequest): Flow<String>
}
