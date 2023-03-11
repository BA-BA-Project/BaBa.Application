package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOneAlbumUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend fun getOneAlbum(id: Int) = flow {
        repository.getAlbum(id).collect{
            emit(it)
        }
    }
}