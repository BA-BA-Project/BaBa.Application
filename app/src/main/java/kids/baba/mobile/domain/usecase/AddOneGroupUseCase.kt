package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.model.MyPageGroup
import kids.baba.mobile.domain.repository.MyPageRepository
import javax.inject.Inject

class AddOneGroupUseCase @Inject constructor(private val repository: MyPageRepository) {
    suspend fun add(myPageGroup: MyPageGroup) = repository.addGroup(myPageGroup = myPageGroup)
}