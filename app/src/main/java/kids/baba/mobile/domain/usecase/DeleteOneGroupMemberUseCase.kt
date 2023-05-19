package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class DeleteOneGroupMemberUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun delete(memberId: String) = repository.deleteGroupMember(memberId = memberId)
}