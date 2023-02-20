package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.auth.AuthRemoteDataSource
import kids.baba.mobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authRemoteDataSource: AuthRemoteDataSource) :
    AuthRepository {
    override suspend fun login(socialToken: String) = flow {
        authRemoteDataSource.login(socialToken).catch {
            throw it
        }.collect {
            emit(it)
        }
    }
}
