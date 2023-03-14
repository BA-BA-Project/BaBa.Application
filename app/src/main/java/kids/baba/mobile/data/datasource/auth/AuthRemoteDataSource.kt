package kids.baba.mobile.data.datasource.auth

import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.TermsData
import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    fun login(socialToken: String): Flow<TokenResponse>

    fun getTerms(socialToken: String): Flow<List<TermsData>>

    fun getSignToken(signTokenRequest: SignTokenRequest): Flow<String>
}
