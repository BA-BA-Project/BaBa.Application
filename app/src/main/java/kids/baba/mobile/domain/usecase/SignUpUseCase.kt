package kids.baba.mobile.domain.usecase

import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.model.TokenResponse
import kids.baba.mobile.domain.repository.MemberRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend fun signUpWithBabiesInfo(signToken: String, signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo): Result<TokenResponse>{
        val result = memberRepository.signUpWithBabiesInfo(signToken, signUpRequestWithBabiesInfo)
        if ( result is Result.Success){
            setJWTToken(result.data)
        }
        return result
    }

    suspend fun signUpWithInviteCode(signToken: String, signUpRequestWithInviteCode: SignUpRequestWithInviteCode) = runCatching {
        val token = memberRepository.signUpWithInviteCode(signToken, signUpRequestWithInviteCode).first()
        setJWTToken(token)
    }

    private fun setJWTToken(token: TokenResponse) {
        EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, token.accessToken)
        EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, token.refreshToken)
    }
}