package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.TermsData
import kids.baba.mobile.domain.model.TokenResponse

interface AuthRepository {
    suspend fun login(socialToken: String): Result<TokenResponse>

    suspend fun getTerms(socialToken: String): Result<List<TermsData>>

    suspend fun getSignToken(signTokenRequest: SignTokenRequest): Result<String>
}
