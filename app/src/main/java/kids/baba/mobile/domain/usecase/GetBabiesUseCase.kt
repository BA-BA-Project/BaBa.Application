package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.BabyRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBabiesUseCase @Inject constructor(private val repository: BabyRepository) {
    suspend fun getBabies() = flow {
        repository.getBaby().collect {
            emit(it)
        }
    }
}