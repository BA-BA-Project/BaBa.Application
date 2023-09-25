package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.TermsData
import kids.baba.mobile.domain.model.TokenResponse

interface AuthRepository {
    suspend fun login(socialToken: String): ApiResult<TokenResponse>

    suspend fun getTerms(socialToken: String): ApiResult<List<TermsData>>

    suspend fun getSignToken(signTokenRequest: SignTokenRequest): ApiResult<String>
}
