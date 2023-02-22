package kids.baba.mobile.data

import com.google.gson.GsonBuilder
import kids.baba.mobile.BuildConfig
import okhttp3.OkHttpClient
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
        }
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
