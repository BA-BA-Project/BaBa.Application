package kids.baba.mobile.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.BuildConfig
import kids.baba.mobile.data.api.AlbumApi
import kids.baba.mobile.data.api.AuthApi
import kids.baba.mobile.data.api.BabyApi
import kids.baba.mobile.data.api.MemberApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BabaRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthRetrofit

    @BabaRetrofit
    @Singleton
    @Provides
    fun provideBabaRetrofit(
        @NetworkModule.BabaClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @AuthRetrofit
    @Singleton
    @Provides
    fun provideRefreshTokenRetrofit(
        @NetworkModule.AuthClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()


    @Singleton
    @Provides
    fun provideAuthApi(
        @AuthRetrofit retrofit: Retrofit
    ): AuthApi = retrofit.create(AuthApi::class.java)

    @Singleton
    @Provides
    fun provideMemberApi(
        @BabaRetrofit retrofit: Retrofit
    ): MemberApi = retrofit.create(MemberApi::class.java)

    @Singleton
    @Provides
    fun babyApi(
        @BabaRetrofit retrofit: Retrofit
    ): BabyApi = retrofit.create(BabyApi::class.java)

    @Singleton
    @Provides
    fun provideAlbumApi(
        @BabaRetrofit retrofit: Retrofit
    ): AlbumApi = retrofit.create(AlbumApi::class.java)

}
