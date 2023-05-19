package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.model.Profile
import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun edit(profile: Profile) = repository.editProfile(profile = profile)
}