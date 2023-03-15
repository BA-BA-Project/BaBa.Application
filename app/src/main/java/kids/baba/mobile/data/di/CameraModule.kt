package kids.baba.mobile.data.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CameraModule {



}

/*
package kids.baba.mobile.data.di


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    abstract fun bindMemberRemoteDataSource(memberRemoteDataSourceImpl: MemberRemoteDataSourceImpl): MemberRemoteDataSource

    @Binds
    abstract fun bindAlbumRemoteDataSource(albumRemoteDataSourceImpl: AlbumRemoteDataSourceImpl): AlbumRemoteDataSource
}

 */