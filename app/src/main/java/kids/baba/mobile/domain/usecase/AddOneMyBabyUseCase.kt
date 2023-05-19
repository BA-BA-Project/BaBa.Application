package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.model.MyBaby
import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class AddOneMyBabyUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun add(baby: MyBaby) = repository.addMyBaby(baby = baby)
}