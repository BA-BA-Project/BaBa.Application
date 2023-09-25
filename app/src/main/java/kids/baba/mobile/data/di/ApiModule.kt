package kids.baba.mobile.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.BuildConfig
import kids.baba.mobile.data.api.*
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

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FileRetrofit

    @BabaRetrofit
    @Singleton
    @Provides
    fun provideBabaRetrofit(
        @NetworkModule.BabaClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = buildBaseRetrofit(okHttpClient, gsonConverterFactory)

    @AuthRetrofit
    @Singleton
    @Provides
    fun provideRefreshTokenRetrofit(
        @NetworkModule.AuthClient
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = buildBaseRetrofit(okHttpClient, gsonConverterFactory)

    @FileRetrofit
    @Singleton
    @Provides
    fun provideFileDownRetrofit(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = buildFileDownRetrofit(gsonConverterFactory)

    private fun buildBaseRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    private fun buildFileDownRetrofit(gsonConverterFactory: GsonConverterFactory) =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_PHOTO_URL)
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

    @Singleton
    @Provides
    fun provideMyPageApi(
        @BabaRetrofit retrofit: Retrofit
    ): MyPageApi = retrofit.create(MyPageApi::class.java)

    @Singleton
    @Provides
    fun provideFileDownApi(
        @FileRetrofit retrofit: Retrofit
    ): FileApi = retrofit.create(FileApi::class.java)
}
