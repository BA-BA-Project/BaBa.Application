package kids.baba.mobile.domain.model

sealed class Result<out T>{
    data class Success<T>(val data: T): Result<T>()
    data class Failure(val code: Int, val message: String?, val throwable: Throwable) : Result<Nothing>()
    data class NetworkError(val throwable: Throwable) : Result<Nothing>()
    data class Unexpected(val throwable: Throwable) : Result<Nothing>()
}

fun <T> Result<T>.getThrowableOrNull() : Throwable? =
    when(this) {
        is Result.Success -> null
        is Result.Failure -> throwable
        is Result.NetworkError -> throwable
        is Result.Unexpected -> throwable
    }
