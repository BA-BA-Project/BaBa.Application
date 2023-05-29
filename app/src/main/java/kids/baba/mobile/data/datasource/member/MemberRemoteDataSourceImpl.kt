package kids.baba.mobile.data.datasource.member

import android.accounts.NetworkErrorException
import kids.baba.mobile.data.api.MemberApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val memberApi: MemberApi,
    private val safeApiHelper: SafeApiHelper
) : MemberRemoteDataSource {
    override suspend fun getMe(accessToken: String) = safeApiHelper.getSafe(
        remoteFetch = { memberApi.getMe(accessToken) },
        mapping = { it }
    )

    override suspend fun signUpWithBabiesInfo(
        signToken: String,
        signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo,
    ) = safeApiHelper.getSafe(
        remoteFetch = { memberApi.signUpWithBabiesInfo(signToken, signUpRequestWithBabiesInfo) },
        mapping = { it }
    )


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
