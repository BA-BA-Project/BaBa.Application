package kids.baba.mobile.data.network

import android.util.Log
import com.google.gson.Gson
import kids.baba.mobile.core.error.InvalidAccessTokenException
import kids.baba.mobile.core.error.NullBodyException
import kids.baba.mobile.core.error.UnKnownException
import kids.baba.mobile.domain.model.ErrorResponse
import kids.baba.mobile.domain.model.ApiResult
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class SafeApiHelperImpl @Inject constructor(
) : SafeApiHelper {
    private val gson = Gson()
    override suspend fun <ResultType, RequestType> getSafe(
        remoteFetch: suspend () -> Response<RequestType>,
        mapping: (RequestType) -> ResultType
    ): ApiResult<ResultType> {

        lateinit var apiResult: ApiResult<ResultType>

        runCatching { remoteFetch() }
            .onSuccess {
                if (it.isSuccessful) {
                    val body = it.body()
                    apiResult = successApiResult<RequestType, ResultType>(body, mapping)
                } else {
                    val errorMessage = setErrorMessage(it)
                    apiResult = errorCodeApiResult(it, errorMessage)
                }
            }
            .onFailure {
                apiResult = when (it) {
                    is IOException -> ApiResult.NetworkError(it)
                    else -> ApiResult.Unexpected(it)
                }
            }

        if ((apiResult as? ApiResult.Failure)?.throwable is InvalidAccessTokenException) {
            return getSafe(remoteFetch, mapping)
        }

        return apiResult
    }

    private fun <RequestType, ResultType> successApiResult(
        body: RequestType?,
        mapping: (RequestType) -> ResultType
    ) = if (body != null) {
        ApiResult.Success(mapping(body))
    } else {
        ApiResult.Unexpected(NullBodyException("Body가 null임"))
    }

    private fun <RequestType> errorCodeApiResult(
        it: Response<RequestType>,
        errorMessage: String?
    ) = if (it.code() == 401) {
        ApiResult.Failure(it.code(), errorMessage, InvalidAccessTokenException())
    } else {
        ApiResult.Failure(it.code(), errorMessage, UnKnownException())
    }

    private fun <RequestType> setErrorMessage(it: Response<RequestType>): String {
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
        return errorMessage
    }
}