package kids.baba.mobile.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.data.ApiHelper
import kids.baba.mobile.data.api.AuthApi
import kids.baba.mobile.data.api.BabyApi
import kids.baba.mobile.data.api.MemberApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideAuthApi() = ApiHelper.create(AuthApi::class.java)

    @Singleton
    @Provides
    fun provideMemberApi() = ApiHelper.create(MemberApi::class.java)

    @Singleton
    @Provides
    fun babyApi() = ApiHelper.create(BabyApi::class.java)
}
