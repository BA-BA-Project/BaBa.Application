package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.model.SignTokenRequest
import kids.baba.mobile.domain.model.TermsDataForSignToken
import kids.baba.mobile.domain.repository.AuthRepository
import javax.inject.Inject

class GetSignTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(socialToken: String, termsData: List<TermsDataForSignToken> ) =
        authRepository.getSignToken(SignTokenRequest(
            socialToken, termsData
        ))

}