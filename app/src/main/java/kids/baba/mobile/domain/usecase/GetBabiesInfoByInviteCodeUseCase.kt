package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.BabyRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetBabiesInfoByInviteCodeUseCase @Inject constructor(
    private val babyRepository: BabyRepository
) {
    suspend operator fun invoke(inviteCode: String) = runCatching {
        babyRepository.getBabiesInfo(inviteCode).first()
    }

}