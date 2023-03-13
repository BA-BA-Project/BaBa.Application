package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.BabyRepository
import javax.inject.Inject

class GetBabiesInfoByInviteCodeUseCase @Inject constructor(
    private val babyRepository: BabyRepository
) {
    suspend operator fun invoke(signToken: String, inviteCode: String) =
        babyRepository.getBabiesInfo(signToken, inviteCode)
}