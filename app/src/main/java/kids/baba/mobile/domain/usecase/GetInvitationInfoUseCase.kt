package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class GetInvitationInfoUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend operator fun invoke(inviteCode: String) = repository.getInvitationInfo(inviteCode = inviteCode)
}