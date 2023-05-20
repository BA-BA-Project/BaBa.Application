package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAllAlbumsUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend operator fun invoke(id: String) = repository.getAllAlbum(id)

}