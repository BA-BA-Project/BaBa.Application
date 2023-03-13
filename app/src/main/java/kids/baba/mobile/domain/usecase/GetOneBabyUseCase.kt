package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.BabyRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOneBabyUseCase @Inject constructor(private val repository: BabyRepository) {
    suspend fun getOneBaby() = flow {
        repository.getBaby().collect {
            emit(it)
        }
    }
}