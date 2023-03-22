package kids.baba.mobile.data.datasource.auth

import kids.baba.mobile.core.error.BadRequest
import kids.baba.mobile.core.error.NetworkErrorException
import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.data.api.AuthApi
import kids.baba.mobile.domain.model.LoginRequest
import kids.baba.mobile.domain.model.SignTokenRequest
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val api: AuthApi) :
    AuthRemoteDataSource {

    override fun login(socialToken: String) = flow {
        runCatching { api.login(LoginRequest(socialToken)) }
            .onSuccess { resp ->
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
                }
            }
            .onFailure {
                if (it is UnknownHostException) {
                    throw NetworkErrorException()
                } else {
                    throw it
                }
            }
    }

    override fun getTerms(socialToken: String) = flow {
        runCatching { api.getTerms(socialToken) }
            .onSuccess { resp ->
                when (resp.code()) {
                    200 -> {
                        val data = resp.body() ?: throw Throwable("data is null")
                        emit(data.terms)
                    }

                    400 -> {
                        throw BadRequest("잘못된 요청")
                    }
                }
            }.onFailure {
                if (it is UnknownHostException) {
                    throw NetworkErrorException()
                } else {
                    throw it
                }
            }
    }

    override fun getSignToken(signTokenRequest: SignTokenRequest) = flow {
        runCatching { api.getSignToken(signTokenRequest) }
            .onSuccess { resp ->
                when (resp.code()) {
                    201 -> {
                        val data = resp.body() ?: throw Throwable("data is null")
                        emit(data.signToken)
                    }

                    400 -> {
                        throw BadRequest("잘못된 요청")
                    }

                    else -> throw Throwable("signToken 요청 에러")
                }
            }.onFailure {
                if(it is UnknownHostException){
                    throw NetworkErrorException()
                } else {
                    throw it
                }
            }

    }
}
