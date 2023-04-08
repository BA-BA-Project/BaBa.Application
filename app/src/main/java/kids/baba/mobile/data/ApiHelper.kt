package kids.baba.mobile.data

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kids.baba.mobile.BuildConfig
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.data.api.AuthApi
import kids.baba.mobile.domain.model.TokenRefreshRequest
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ApiHelper {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

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

private class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(formatter.format(src))
    }
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
        return formatter.parse(json!!.asString, LocalDateTime::from)
    }
}

private class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(formatter.format(src))
    }
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
        return formatter.parse(json!!.asString, LocalDate::from)
    }
}

private class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val hasAuthorization = chain.request().headers.names().contains("Authorization")

        if (hasAuthorization) {
            val accessToken = chain.request().header("Authorization")
            request.header("Authorization", "Bearer $accessToken")
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
