package kids.baba.mobile.data.datasource.member

import kids.baba.mobile.data.api.MemberApi
import kids.baba.mobile.domain.model.SignUpRequest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(private val memberApi: MemberApi) :
    MemberRemoteDataSource {
    override suspend fun getMe(accessToken: String) = flow {
        val resp = memberApi.getMe(accessToken)

        if (resp.isSuccessful) {
            val data = resp.body() ?: throw Throwable("서버로부터 받은 사용자 정보가 null임")
            emit(data)
        } else {
            throw Throwable("사용자 정보를 받아올 수 없음.")
        }
    }

    override suspend fun signUp(signToken: String, signUpRequest: SignUpRequest) = flow {
        val resp = memberApi.signUp(signToken, signUpRequest)

        resp.headers()
        when (resp.code()) {
            201 -> {
                val data = resp.body() ?: throw Throwable("data is null")
                emit(data)
            }
            else -> throw Throwable("회원가입 에러")
        }
    }
}
