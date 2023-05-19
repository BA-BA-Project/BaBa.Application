package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class DeleteOneGroupUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun delete(groupName: String) = repository.deleteGroup(groupName = groupName)
}