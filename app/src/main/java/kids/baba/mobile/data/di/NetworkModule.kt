package kids.baba.mobile.data.di

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.BuildConfig
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.error.TokenEmptyException
import kids.baba.mobile.core.error.TokenRefreshFailedException
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.data.api.AuthApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.data.network.SafeApiHelperImpl
import kids.baba.mobile.data.util.LocalDateAdapter
import kids.baba.mobile.data.util.LocalDateTimeAdapter
import kids.baba.mobile.domain.model.TokenRefreshRequest
import kids.baba.mobile.presentation.view.activity.IntroActivity
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BabaClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthClient

    @Singleton
    @Provides
    fun provideGsonBuilder(): GsonBuilder {
        return GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
    }

    @Singleton
    @Provides
    fun provideConverterFactory(
        gsonBuilder: GsonBuilder
    ): GsonConverterFactory {
        return GsonConverterFactory.create(
            gsonBuilder.create()
        )
    }

    @BabaClient
    @Singleton
    @Provides
    fun provideBabaClient(
        authorizationInterceptor: Interceptor,
        tokenAuthenticator: Authenticator
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(authorizationInterceptor)
        }
        builder.authenticator(tokenAuthenticator)
        return builder.build()
    }

    @AuthClient
    @Singleton
    @Provides
    fun provideAuthClient(
        authorizationInterceptor: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(authorizationInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun providesAuthorizationInterceptor() = Interceptor { chain ->
        val request = chain.request().newBuilder()
        val hasAuthorization = chain.request().headers.names().contains("Authorization")

        if (hasAuthorization) {
            val accessToken = chain.request().header("Authorization")
            request.header("Authorization", "Bearer $accessToken")
        }
        chain.proceed(request.build())
    }

    @Singleton
    @Provides
    fun provideTokenAuthenticator(
        @ApplicationContext context: Context,
        authApi: AuthApi
    ) = Authenticator { _, response ->
        val tag = "TokenAuthenticator"
        val isPathRefresh =
            response.request.url.toUrl().toString() == BuildConfig.BASE_URL + "auth/refresh"
        if (response.code == 401 && !isPathRefresh) {
            try {
                val refreshToken = EncryptedPrefs.getString(PrefsKey.REFRESH_TOKEN_KEY)
                val tokenRefreshRequest = TokenRefreshRequest(refreshToken)
                val resp = authApi.tokenRefresh(tokenRefreshRequest).execute()
                EncryptedPrefs.clearPrefs()

                if (!resp.isSuccessful) {
                    IntroActivity.startActivity(context)
                    throw TokenRefreshFailedException("토큰 갱신 실패")
                }

                val token = resp.body() ?: throw TokenEmptyException("받아온 토큰 값이 null임")

                EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, token.accessToken)
                EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, token.refreshToken)

                response.request.newBuilder().apply {
                    removeHeader("Authorization")
                    addHeader("Authorization", "Bearer ${token.accessToken}")
                }.build()
            } catch (e: Exception) {
                Log.e(tag, e.message.toString(), e)
            }
        }
        null
    }

    @Singleton
    @Provides
    fun provideSafeApi() : SafeApiHelper = SafeApiHelperImpl()

}