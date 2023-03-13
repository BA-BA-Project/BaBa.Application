package kids.baba.mobile.data.datasource.auth

import android.util.Log
import kids.baba.mobile.core.error.BadRequest
import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.data.api.AuthApi
import kids.baba.mobile.domain.model.LoginRequest
import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.TermsData
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val api: AuthApi) :
    AuthRemoteDataSource {

    override suspend fun login(socialToken: String) = flow {
        val resp = api.login(LoginRequest(socialToken))

        when (resp.code()) {
            200 -> {
                val data = resp.body() ?: throw Throwable("data is null")
                emit(data)
            }
            404 -> {
                throw UserNotFoundException(
                    message = "신규 로그인"
                )
            }
            else -> throw Throwable("로그인 에러")
        }
    }

    override suspend fun getTerms(socialToken: String): List<TermsData> {
        val resp = api.getTerms(socialToken)

        Log.d("termsLog",resp.code().toString())
        when (resp.code()) {
            200 -> {
                return resp.body() ?: throw Throwable("data is null")
            }
            400 -> {
                throw BadRequest("잘못된 요청")
            }
            else -> throw Throwable("약관 요청 에러")
        }
    }

    override suspend fun getSignToken(signTokenRequest: SignTokenRequest): String {
        val resp = api.getSignToken(signTokenRequest)

        when(resp.code()) {
            201 -> {
                return resp.body() ?: throw Throwable("data is null")
            }
            400 -> {
                throw BadRequest("잘못된 요청")
            }
            else -> throw Throwable("signToken 요청 에러")
        }
    }
}
