package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.BabyRepository
import javax.inject.Inject

class GetBabiesUseCase @Inject constructor(private val repository: BabyRepository) {
    suspend operator fun invoke() = repository.getBaby()
}