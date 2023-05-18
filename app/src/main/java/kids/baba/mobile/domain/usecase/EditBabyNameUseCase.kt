package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class EditBabyNameUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun edit(babyId: String, name: String) =
        repository.editBabyName(babyId = babyId, name = name)
}