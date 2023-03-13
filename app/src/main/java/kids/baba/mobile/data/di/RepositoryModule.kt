package kids.baba.mobile.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.data.repository.AuthRepositoryImpl
import kids.baba.mobile.data.repository.BabyRepositoryImpl
import kids.baba.mobile.data.repository.KakaoLoginImpl
import kids.baba.mobile.data.repository.MemberRepositoryImpl
import kids.baba.mobile.domain.repository.AuthRepository
import kids.baba.mobile.domain.repository.BabyRepository
import kids.baba.mobile.domain.repository.KakaoLogin
import kids.baba.mobile.domain.repository.MemberRepository

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
    abstract fun bindBabyRepository(babyRepositoryImpl: BabyRepositoryImpl): BabyRepository
}
