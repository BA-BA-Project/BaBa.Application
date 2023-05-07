package kids.baba.mobile.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.data.datasource.album.AlbumRemoteDataSource
import kids.baba.mobile.data.datasource.album.AlbumRemoteDataSourceImpl
import kids.baba.mobile.data.datasource.auth.AuthRemoteDataSource
import kids.baba.mobile.data.datasource.auth.AuthRemoteDataSourceImpl
import kids.baba.mobile.data.datasource.baby.BabyRemoteDataSource
import kids.baba.mobile.data.datasource.baby.BabyRemoteDataSourceImpl
import kids.baba.mobile.data.datasource.member.MemberRemoteDataSource
import kids.baba.mobile.data.datasource.member.MemberRemoteDataSourceImpl
import kids.baba.mobile.data.datasource.mypage.MyPageRemoteDataSource
import kids.baba.mobile.data.datasource.mypage.MyPageRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    abstract fun bindMemberRemoteDataSource(memberRemoteDataSourceImpl: MemberRemoteDataSourceImpl): MemberRemoteDataSource

    @Binds
    abstract fun bindAlbumRemoteDataSource(albumRemoteDataSourceImpl: AlbumRemoteDataSourceImpl): AlbumRemoteDataSource

    @Binds
    abstract fun bindBabyRemoteDataSource(babyRemoteDataSourceImpl: BabyRemoteDataSourceImpl): BabyRemoteDataSource

    @Binds
    abstract fun bindMyPageRemoteDataSource(myPageRemoteDataSourceImpl: MyPageRemoteDataSourceImpl): MyPageRemoteDataSource
}
