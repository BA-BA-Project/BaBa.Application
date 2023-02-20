package kids.baba.mobile.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.data.ApiHelper
import kids.baba.mobile.data.api.AuthApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideAuthApi() = ApiHelper.create(AuthApi::class.java)
}
