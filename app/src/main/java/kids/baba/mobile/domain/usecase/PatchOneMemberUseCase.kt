package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.model.GroupMemberInfo
import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class PatchOneMemberUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun patch(memberId: String, relation: GroupMemberInfo) =
        repository.patchMember(memberId = memberId, relation = relation)
}