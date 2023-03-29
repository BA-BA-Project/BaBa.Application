package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAlbumsFromBabyIdUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend fun getOneAlbum(id: String, year: Int, month: Int, token: String) = flow {
        repository.getAlbum(id, year, month, token).collect {
            emit(it)
        }
    }
}