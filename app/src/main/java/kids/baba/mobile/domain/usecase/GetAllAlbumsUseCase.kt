package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllAlbumsUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend fun getAllAlbumsFromBabyId(id: String) = flow {
        repository.getAllAlbum(id).collect {
            emit(it)
        }
    }

}