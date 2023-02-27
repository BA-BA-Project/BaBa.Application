package kids.baba.mobile.data.repository

import android.content.Context
import android.util.Log
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kids.baba.mobile.core.error.kakao.KakaoLoginCanceledException
import kids.baba.mobile.core.error.kakao.KakaoTokenEmptyException
import kids.baba.mobile.core.error.kakao.KakaoUserNotFoundException
import kids.baba.mobile.domain.repository.KakaoLogin
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class KakaoLoginImpl @Inject constructor(@ApplicationContext private val context: Context) :
    KakaoLogin {
    private val tag = "kakaoLogin"

    override suspend fun login(): Result<String> = runCatching {
        val isInstalledKakaoTalk = UserApiClient.instance.isKakaoTalkLoginAvailable(context)
        Log.i(tag, "카카오톡 설치 여부: $isInstalledKakaoTalk")
        if (isInstalledKakaoTalk) {
            try {
                loginWithKakaoTalk()
            } catch (e: KakaoUserNotFoundException) {
                loginWithKakaoAccount()
            }
        } else {
            loginWithKakaoAccount()
        }
    }

    private suspend fun loginWithKakaoTalk() = callbackFlow {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            when {
                error != null -> {
                    val exception = when {
                        error is ClientError && error.reason == ClientErrorCause.Cancelled ->
                            KakaoLoginCanceledException("사용자에 의한 카카오 로그인 취소")

                        else -> {
                            KakaoUserNotFoundException("카카오톡에 연결된 카카오 계정이 존재하지 않으므로 카카오계정으로 로그인 시도")
                        }
                    }
                    close(exception)
                }

                token == null -> close(KakaoTokenEmptyException("카카오에서 받은 token이 null임."))
                else -> {
                    trySend(token.accessToken)
                    close()
                }
            }
        }
        awaitClose()
    }.first()

    private suspend fun loginWithKakaoAccount() = callbackFlow {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            when {
                error != null -> close(error)
                token == null -> close(KakaoTokenEmptyException("카카오에서 받은 token이 null임."))
                else -> {
                    trySend(token.accessToken)
                    close()
                }
            }
        }
        awaitClose()
    }.first()
}
