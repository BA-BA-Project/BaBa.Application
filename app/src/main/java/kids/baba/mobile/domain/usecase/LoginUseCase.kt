package kids.baba.mobile.domain.usecase

import android.util.Log
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    private val TAG = "LoginUseCase"

    suspend fun login(socialToken: String) {
        authRepository.login(socialToken).catch {
            if (it is UserNotFoundException) {
                Log.i(TAG, it.message.toString())
                EncryptedPrefs.putString(PrefsKey.SIGN_TOKEN_KEY, it.signToken)
            } else {
                Log.e(TAG, "로그인 실패 - ${it.message}")
            }
            throw it
        }.collect {
            EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, it.accessToken)
            EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, it.refreshToken)
        }
    }
}
