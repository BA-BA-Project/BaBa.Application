package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.model.Group
import kids.baba.mobile.domain.model.GroupInfo
import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class PatchOneGroupUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun patch(group: GroupInfo, groupName: String) =
        repository.patchGroup(group = group, groupName = groupName)
}