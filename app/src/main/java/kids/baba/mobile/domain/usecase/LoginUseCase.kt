package kids.baba.mobile.domain.usecase

import android.util.Log
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.TokenResponse
import kids.baba.mobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    private val tag = "LoginUseCase"

    suspend fun login(socialToken: String) {
        authRepository.login(socialToken).catch {
            if (it is UserNotFoundException) {
                Log.i(tag, it.message.toString())
                setJwtToken(it.tokenResponse)
            } else {
                Log.e(tag, "로그인 실패 - ${it.message}")
            }
            throw it
        }.collect { token ->
            setJwtToken(token)
        }
    }

    private fun setJwtToken(tokenResponse: TokenResponse) {
        EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, tokenResponse.accessToken)
        EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, tokenResponse.refreshToken)
    }
}
