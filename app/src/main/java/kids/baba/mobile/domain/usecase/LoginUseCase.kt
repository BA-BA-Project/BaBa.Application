package kids.baba.mobile.domain.usecase

import android.content.Context
import android.util.Log
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.TokenResponse
import kids.baba.mobile.domain.repository.AuthRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    private val tag = "LoginUseCase"

    suspend fun login(context: Context) {
        val isInstalledKakaoTalk = UserApiClient.instance.isKakaoTalkLoginAvailable(context)
        Log.i(tag, "카카오톡 설치 여부: $isInstalledKakaoTalk")
        if (isInstalledKakaoTalk) {
            loginWithKakaoTalk(context)
        } else {
            loginWithKakaoAccount(context)
        }
    }

    @OptIn(FlowPreview::class)
    private suspend fun loginWithKakaoTalk(context: Context) {
        loginWithKakaoTalkCallback(context).catch {
            if (it is ClientError && it.reason == ClientErrorCause.Cancelled) {
                Log.e(tag, "사용자에 의한 카카오 로그인 취소")
                throw it
            } else {
                Log.w(tag, "카카오톡에 연결된 카카오 계정이 존재하지 않으므로 카카오계정으로 로그인 시도")
                loginWithKakaoAccount(context)
            }
        }.flatMapConcat { socialToken ->
            Log.i(tag, "서버에 JWT토큰 발급 요청")
            authRepository.login(socialToken)
        }.catch {
            throw it
        }.collect { token ->
            Log.i(tag, "서버에서 JWT토큰 발급 완료")
            setJWTToken(token)
        }
    }

    @OptIn(FlowPreview::class)
    private suspend fun loginWithKakaoAccount(context: Context) {
        loginWithKakaoAccountCallback(context).catch {
            Log.e(tag, "로그인 실패", it)
            throw it
        }.flatMapConcat { socialToken ->
            Log.i(tag, "서버에 JWT토큰 발급 요청")
            authRepository.login(socialToken)
        }.catch {
            throw it
        }.collect { token ->
            Log.i(tag, "서버에서 JWT토큰 발급 완료")
            setJWTToken(token)
        }
    }

    private fun loginWithKakaoTalkCallback(context: Context) = callbackFlow {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) throw error
            if (token == null) throw Throwable("카카오에서 받은 token이 null임.")
            trySend(token.accessToken)
        }
        awaitClose()
    }

    private fun loginWithKakaoAccountCallback(context: Context) = callbackFlow {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) throw error
            if (token == null) throw Throwable("카카오에서 받은 token이 null임.")
            trySend(token.accessToken)
        }
        awaitClose()
    }

    private fun setJWTToken(token: TokenResponse) {
        EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, token.accessToken)
        EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, token.refreshToken)
    }
}
