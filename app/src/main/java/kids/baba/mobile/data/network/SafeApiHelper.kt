package kids.baba.mobile.data.network

import kids.baba.mobile.domain.model.ApiResult
import retrofit2.Response

interface SafeApiHelper {
    suspend fun <ResultType, RequestType> getSafe(
        remoteFetch: suspend () -> Response<RequestType>,
        mapping: (RequestType) -> (ResultType),
    ): ApiResult<ResultType>

}