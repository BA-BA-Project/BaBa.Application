package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.album.AlbumRemoteDataSource
import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.domain.repository.BabyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BabyRepositoryImpl @Inject constructor(private val dataSource: AlbumRemoteDataSource) :
    BabyRepository {
    override suspend fun getBaby(): Flow<BabyResponse> = dataSource.getBaby()

}