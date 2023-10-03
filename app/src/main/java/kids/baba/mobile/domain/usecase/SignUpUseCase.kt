package kids.baba.mobile.domain.usecase

import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.model.TokenResponse
import kids.baba.mobile.domain.repository.SignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
) {
    suspend fun signUpWithBabiesInfo(
        signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo
    ): ApiResult<TokenResponse> {
        val result = signUpRepository.signUpWithBabiesInfo(signUpRequestWithBabiesInfo)
        if (result is ApiResult.Success) {
            setJWTToken(result.data)
        }
        return result
    }

    suspend fun signUpWithInviteCode(
        signUpRequestWithInviteCode: SignUpRequestWithInviteCode
    ): ApiResult<TokenResponse> {
        val result = signUpRepository.signUpWithInviteCode(signUpRequestWithInviteCode)
        if (result is ApiResult.Success) {
            setJWTToken(result.data)
        }
        return result
    }

    private fun setJWTToken(token: TokenResponse) {
        EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, token.accessToken)
        EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, token.refreshToken)
    }
}