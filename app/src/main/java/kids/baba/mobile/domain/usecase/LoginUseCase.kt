package kids.baba.mobile.domain.usecase

import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.TokenResponse
import kids.baba.mobile.domain.repository.AuthRepository
import kids.baba.mobile.domain.repository.KakaoLogin
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val kakaoLogin: KakaoLogin
) {

    suspend fun babaLogin(socialToken: String): Result<TokenResponse> {
        val result = authRepository.login(socialToken)
        if (result is Result.Success) {
            setJWTToken(result.data)
        }
        return result
    }

    suspend fun kakaoLogin() = kakaoLogin.login()

    private fun setJWTToken(token: TokenResponse) {
        EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, token.accessToken)
        EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, token.refreshToken)
    }
}
