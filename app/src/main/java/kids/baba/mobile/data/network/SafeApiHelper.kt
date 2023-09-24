package kids.baba.mobile.data.network

import kids.baba.mobile.domain.model.Result
import retrofit2.Response

interface SafeApiHelper {
    suspend fun <ResultType, RequestType> getSafe(
        remoteFetch: suspend () -> Response<RequestType>,
        mapping: (RequestType) -> (ResultType),
    ): Result<ResultType>

}