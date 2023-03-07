package kids.baba.mobile.data.datasource.auth

import kids.baba.mobile.domain.model.TermsData
import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    suspend fun login(socialToken: String): Flow<TokenResponse>

    suspend fun getTerms(socialToken: String): List<TermsData>
}
