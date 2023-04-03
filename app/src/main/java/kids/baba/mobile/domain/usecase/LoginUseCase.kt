package kids.baba.mobile.domain.usecase

import android.util.Log
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.TokenResponse
import kids.baba.mobile.domain.repository.AuthRepository
import kids.baba.mobile.domain.repository.KakaoLogin
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val kakaoLogin: KakaoLogin
) {
    private val tag = "LoginUseCase"

    suspend fun babaLogin(socialToken: String) = runCatching {
        authRepository.login(socialToken).collect { token ->
            Log.i(tag, "서버에서 JWT토큰 발급 완료")
            setJWTToken(token)
        }
    }

    suspend fun kakaoLogin() = runCatching {
        kakaoLogin.login().getOrThrow()
    }

    private fun setJWTToken(token: TokenResponse) {
        EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, token.accessToken)
        EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, token.refreshToken)
    }
}
