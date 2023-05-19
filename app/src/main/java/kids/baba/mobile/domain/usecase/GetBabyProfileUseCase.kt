package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class GetBabyProfileUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun get(babyId: String) = repository.loadBabyProfile(babyId = babyId)
}