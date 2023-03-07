package kids.baba.mobile.data.repository

import android.util.Log
import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.data.datasource.auth.AuthRemoteDataSource
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authRemoteDataSource: AuthRemoteDataSource) :
    AuthRepository {
    private val tag = "AuthRepositoryImpl"

    override suspend fun login(socialToken: String) = flow {
        authRemoteDataSource.login(socialToken).catch {
            if (it is UserNotFoundException) {
                Log.i(tag, it.message.toString())
            } else {
                Log.e(tag, "로그인 실패 - ${it.message}")
            }
            throw it
        }.collect {
            emit(it)
        }
    }

    override suspend fun getTerms(socialToken: String) = runCatching {
        authRemoteDataSource.getTerms(socialToken)
    }

    override suspend fun getSignToken(signTokenRequest: SignTokenRequest) = kotlin.runCatching {
        authRemoteDataSource.getSignToken(signTokenRequest)
    }
}
