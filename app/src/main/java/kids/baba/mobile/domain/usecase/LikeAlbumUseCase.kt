package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class LikeAlbumUseCase @Inject constructor(private val repository: AlbumRepository) {
    suspend operator fun invoke(id: String, contentId: String) = repository.likeAlbum(id, contentId)
}