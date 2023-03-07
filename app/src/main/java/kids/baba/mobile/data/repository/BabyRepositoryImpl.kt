package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.album.AlbumRemoteDataSource
import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.domain.repository.BabyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BabyRepositoryImpl @Inject constructor(private val dataSource: AlbumRemoteDataSource) :
    BabyRepository {
    override suspend fun getBaby(): Flow<Baby> = dataSource.getBaby()
}