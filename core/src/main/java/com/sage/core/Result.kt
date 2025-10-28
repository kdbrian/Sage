package com.sage.core


class AppError(override val cause: Throwable?) : Throwable(cause = cause)


sealed class Result<out D, out E> {
    data class Success<out D>(val data: D) : Result<D, Nothing>()
    data class Error(val error: AppError) : Result<Nothing, Nothing>()
    data class BusinessRuleError<out E>(val error: E) : Result<Nothing, E>()
    data object Loading : Result<Nothing, Nothing>()

    fun isSuccessful() = this is Success
    fun hasFailed() = this is Error || this is BusinessRuleError<*>
    fun isLoading() = this is Loading

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
            is BusinessRuleError<*> -> "BusinessRuleError[error=$error]"
            Loading -> "Loading"
        }
    }
}
