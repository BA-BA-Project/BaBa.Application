package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.model.RelationInfo
import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class MakeInviteCodeUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend operator fun invoke(relationInfo: RelationInfo) = repository.makeInviteCode(relationInfo = relationInfo)
}