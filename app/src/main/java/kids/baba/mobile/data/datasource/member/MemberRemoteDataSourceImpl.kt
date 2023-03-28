package kids.baba.mobile.data.datasource.member

import android.accounts.NetworkErrorException
import kids.baba.mobile.data.api.MemberApi
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(private val memberApi: MemberApi) :
    MemberRemoteDataSource {
    override fun getMe(accessToken: String) = flow {
        runCatching { memberApi.getMe(accessToken) }
            .onSuccess { resp ->
                if (resp.isSuccessful) {
                    val data = resp.body() ?: throw Throwable("서버로부터 받은 사용자 정보가 null임")

                    emit(data)
                } else {
                    throw Throwable("사용자 정보를 받아올 수 없음.")
                }
            }.onFailure {
                if(it is UnknownHostException) {
                    throw NetworkErrorException()
                }
                else {
                    throw it
                }
            }

    }

    override fun signUpWithBabiesInfo(
        signToken: String,
        signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo
    ) = flow {
        runCatching {
            memberApi.signUpWithBabiesInfo(signToken, signUpRequestWithBabiesInfo)
        }.onSuccess { resp ->
            when (resp.code()) {
                201 -> {
                    val data = resp.body() ?: throw Throwable("data is null")
                    emit(data)
                }

                else -> throw Throwable("회원가입 에러")
            }
        }.onFailure {
            if (it is UnknownHostException) {
                throw NetworkErrorException()
            } else {
                throw it
            }
        }
    }

    override fun signUpWithInviteCode(
        signToken: String,
        signUpRequestWithInviteCode: SignUpRequestWithInviteCode
    ) = flow {
        runCatching { memberApi.signUpWithInviteCode(signToken, signUpRequestWithInviteCode) }
            .onSuccess { resp ->
                when (resp.code()) {
                    201 -> {
                        val data = resp.body() ?: throw Throwable("data is null")
                        emit(data)
                    }

                    else -> throw Throwable("회원가입 에러")
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
}
