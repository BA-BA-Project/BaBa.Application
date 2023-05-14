package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumsFromBabyIdUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend operator fun invoke(id: String, year: Int, month: Int) = repository.getAlbum(id, year, month)
}