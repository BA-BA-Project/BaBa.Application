package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.auth.AuthRemoteDataSource
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authRemoteDataSource: AuthRemoteDataSource) :
    AuthRepository {

    override suspend fun login(socialToken: String) = authRemoteDataSource.login(socialToken)
    override suspend fun getTerms(socialToken: String) = authRemoteDataSource.getTerms(socialToken)

    override fun getSignToken(signTokenRequest: SignTokenRequest) = flow {
        authRemoteDataSource.getSignToken(signTokenRequest).collect{
            emit(it)
        }
    }
}
