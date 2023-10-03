package kids.baba.mobile.domain.model

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Failure(val code: Int, val message: String?, val throwable: Throwable) : ApiResult<Nothing>()
    data class NetworkError(val throwable: Throwable) : ApiResult<Nothing>()
    data class Unexpected(val throwable: Throwable) : ApiResult<Nothing>()
}

fun <T> ApiResult<T>.getThrowableOrNull(): Throwable? =
    when (this) {
        is ApiResult.Success -> null
        is ApiResult.Failure -> throwable
        is ApiResult.NetworkError -> throwable
        is ApiResult.Unexpected -> throwable
    }
