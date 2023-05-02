package kids.baba.mobile.data.datasource.auth

import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.data.api.AuthApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.domain.model.LoginRequest
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.TokenResponse
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApi,
    private val safeApiHelper: SafeApiHelper
) : AuthRemoteDataSource {
    override suspend fun login(socialToken: String): Result<TokenResponse> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.login(LoginRequest(socialToken))
            },
            mapping = {
                it
            }
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, UserNotFoundException("신규 로그인"))
        } else {
            result
        }
    }

    override suspend fun getTerms(socialToken: String) =
        safeApiHelper.getSafe(
            remoteFetch = {
                api.getTerms(socialToken)
            },
            mapping = { it.terms }
        )

    override suspend fun getSignToken(signTokenRequest: SignTokenRequest) =
        safeApiHelper.getSafe(
            remoteFetch = {
                api.getSignToken(signTokenRequest)
            },
            mapping = {it.signToken}
        )
}
