package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.model.InviteCode
import kids.baba.mobile.domain.model.MyBaby
import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class AddOneBabyWithInviteCodeUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun add(inviteCode: InviteCode) = repository.addBabyWithInviteCode(inviteCode = inviteCode)
}