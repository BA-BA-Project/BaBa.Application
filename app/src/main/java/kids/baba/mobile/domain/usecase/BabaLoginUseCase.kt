package kids.baba.mobile.domain.usecase

import android.util.Log
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.TokenResponse
import kids.baba.mobile.domain.repository.AuthRepository
import kids.baba.mobile.domain.repository.KakaoLogin
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class BabaLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val kakaoLogin: KakaoLogin
    ) {
    private val tag = "LoginUseCase"

    suspend fun login() = runCatching {
        val accessToken = kakaoLogin.login().getOrThrow()
        authRepository.login(accessToken).catch {
            throw it
        }.collect { token ->
            Log.i(tag, "서버에서 JWT토큰 발급 완료")
            setJWTToken(token)
        }
    }

    private fun setJWTToken(token: TokenResponse) {
        EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, token.accessToken)
        EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, token.refreshToken)
    }
}
