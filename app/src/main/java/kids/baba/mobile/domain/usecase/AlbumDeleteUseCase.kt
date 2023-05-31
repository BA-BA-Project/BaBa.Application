package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class AlbumDeleteUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend operator fun invoke(babyId: String, contentId: Int) =
        repository.deleteAlbum(babyId, contentId)

}