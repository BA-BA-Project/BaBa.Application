package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AuthRepository
import javax.inject.Inject

class GetTermsListUseCase @Inject constructor(
    private val authRepository: AuthRepository
    ){
    operator fun invoke(socialToken: String) = authRepository.getTerms(socialToken)
}