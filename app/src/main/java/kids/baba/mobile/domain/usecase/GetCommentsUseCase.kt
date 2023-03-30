package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(private val repository: AlbumRepository) {
    suspend fun get(contentId: String) = repository.getComment(contentId)
}