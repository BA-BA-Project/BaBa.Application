package kids.baba.mobile.data.network

import android.util.Log
import com.google.gson.Gson
import kids.baba.mobile.core.error.NullBodyException
import kids.baba.mobile.domain.model.ErrorResponse
import kids.baba.mobile.domain.model.Result
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class SafeApiHelperImpl @Inject constructor(
) : SafeApiHelper {
    private val gson = Gson()
    override suspend fun <ResultType, RequestType> getSafe(
        remoteFetch: suspend () -> Response<RequestType>,
        mapping: (RequestType) -> ResultType
    ): Result<ResultType> {

        lateinit var result: Result<ResultType>
        runCatching { remoteFetch() }
            .onSuccess {
                if (it.isSuccessful) {
                    val body = it.body()
                    result = if (body != null) {
                        Result.Success(mapping(body))
                    } else {
                        Result.Unexpected(NullBodyException("Body가 null임"))
                    }
                } else {
                    Log.e("SafeApiHelperImpl", "Respone: $it \n ErrorBody: ${it.errorBody()?.string()!!}")
                    var errorMessage = try {
                        gson.fromJson(
                            it.errorBody()?.string(),
                            ErrorResponse::class.java
                        )?.message
                    } catch (e: Exception) {
                        null
                    }


                    if (errorMessage.isNullOrBlank()) {
                        errorMessage = "Unknown Error"
                    }
                    result = Result.Failure(it.code(), errorMessage,null)
                }
            }
            .onFailure {
                result = when (it) {
                    is IOException -> Result.NetworkError(it)
                    else -> Result.Unexpected(it)
                }
            }
        return result
    }
}