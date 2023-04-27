package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.auth.AuthRemoteDataSource
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.repository.AuthRepository
import kids.baba.mobile.presentation.mapper.toPresentation
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authRemoteDataSource: AuthRemoteDataSource) :
    AuthRepository {

    override suspend fun login(socialToken: String) = authRemoteDataSource.login(socialToken)
    override fun getTerms(socialToken: String) = flow {
        authRemoteDataSource.getTerms(socialToken).collect {
            val termsUiModelList = it.map { termsData ->
                termsData.toPresentation()
            }
            emit(termsUiModelList)
        }
    }

    override fun getSignToken(signTokenRequest: SignTokenRequest) = flow {
        authRemoteDataSource.getSignToken(signTokenRequest).collect{
            emit(it)
        }
    }
}
