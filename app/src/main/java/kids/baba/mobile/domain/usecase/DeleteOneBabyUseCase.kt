package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class DeleteOneBabyUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun delete(babyId: String) = repository.deleteBaby(babyId = babyId)
}