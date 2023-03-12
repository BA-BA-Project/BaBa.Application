package kids.baba.mobile.domain.usecase

import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.SignUpRequest
import kids.baba.mobile.domain.model.TokenResponse
import kids.baba.mobile.domain.repository.AuthRepository
import kids.baba.mobile.domain.repository.MemberRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend fun signUp(signToken: String, signUpRequest: SignUpRequest) = runCatching {
        val token = memberRepository.signUp(signToken, signUpRequest).first()
        setJWTToken(token)
        memberRepository.getMe(token.accessToken).first()
    }

    private fun setJWTToken(token: TokenResponse) {
        EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, token.accessToken)
        EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, token.refreshToken)
    }
}