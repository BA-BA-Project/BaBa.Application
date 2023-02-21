package kids.baba.mobile.data

import android.util.Log
import com.google.gson.GsonBuilder
import kids.baba.mobile.BuildConfig
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.data.api.AuthApi
import kids.baba.mobile.domain.model.TokenRefreshRequest
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiHelper {
    private val gson = GsonBuilder().setLenient().create()

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(AuthorizationInterceptor())
        }
        builder.authenticator(TokenAuthenticator())
        return builder.build()
    }

    fun <T> create(
        service: Class<T>,
        baseUrl: String = BuildConfig.BASE_URL
    ): T = Retrofit.Builder().apply {
        baseUrl(baseUrl)
        addConverterFactory(GsonConverterFactory.create(gson))
        client(createOkHttpClient())
    }.build().create(service)
}

private class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val hasAuthorization = chain.request().headers.names().contains("Authorization")
        val accessToken = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY)

        if (hasAuthorization) {
            request.removeHeader("Authorization")
            request.addHeader("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(request.build())
    }
}

private class TokenAuthenticator : Authenticator {
    private val tag = "TokenAuthenticator"
    override fun authenticate(route: Route?, response: Response): Request? {
        val isPathRefresh = response.request.url.toUrl().path == "api/auth/refresh"

        if (response.code == 401 && !isPathRefresh) {
            try {
                val refreshToken = EncryptedPrefs.getString(PrefsKey.REFRESH_TOKEN_KEY)
                val tokenRefreshRequest = TokenRefreshRequest(refreshToken)
                val authApi = ApiHelper.create(AuthApi::class.java)
                val resp = authApi.tokenRefresh(tokenRefreshRequest)

                if (!resp.isSuccessful) {
                    throw Throwable("토큰 갱신 실패")
                }

                val token = resp.body() ?: throw Throwable("받아온 토큰 값이 null임")

                EncryptedPrefs.putString(PrefsKey.ACCESS_TOKEN_KEY, token.accessToken)
                EncryptedPrefs.putString(PrefsKey.REFRESH_TOKEN_KEY, token.refreshToken)

                return response.request.newBuilder().apply {
                    removeHeader("Authorization")
                    addHeader("Authorization", "Bearer ${token.accessToken}")
                }.build()
            } catch (e: Exception) {
                Log.e(tag, e.message.toString(), e)
                return null
            }
        }
        return null
    }
}
