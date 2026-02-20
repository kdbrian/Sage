package com.kdbrian.sage.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
){
    class Idle<T> : Resource<T>()
    data class Loading<T>(val progress: Float) : Resource<T>()  // 0.0 - 1.0
    class Success<T>(data: T?) : Resource<T>(data = data, message = null)
    class Error<T>(message: String?) : Resource<T>(message=message, data = null)
}