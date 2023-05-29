package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class GetLikeDetailUseCase @Inject constructor(private val repository: AlbumRepository) {
    suspend operator fun invoke(id: String, contentId: Int) = repository.getLikeDetail(id = id, contentId = contentId)
}