package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyPageGroupUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun get() = repository.loadMyPageGroup()
}