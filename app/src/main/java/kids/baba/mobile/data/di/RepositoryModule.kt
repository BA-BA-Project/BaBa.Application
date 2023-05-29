package kids.baba.mobile.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.data.repository.*
import kids.baba.mobile.domain.repository.*

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindKakaoLogin(kakaoLoginImpl: KakaoLoginImpl): KakaoLogin

    @Binds
    abstract fun bindMemberRepository(memberRepositoryImpl: MemberRepositoryImpl): MemberRepository

    @Binds
    abstract fun bindAlbumRepository(albumRepositoryImpl: AlbumRepositoryImpl): AlbumRepository

    @Binds
    abstract fun bindBabyRepository(babyRepositoryImpl: BabyRepositoryImpl): BabyRepository

    @Binds
    abstract fun bindPhotoPickerRepository(photoPickerRepositoryImpl: PhotoPickerRepositoryImpl): PhotoPickerRepository

    @Binds
    abstract fun bindMyPageRepository(myPageRepositoryImpl: MyPageRepositoryImpl): MyPageRepository

    @Binds
    abstract fun bindFileRemoteRepository(fileRepositoryImpl: FileRepositoryImpl): FileRepository
}
