package com.kdbrian.sage.util

import android.content.Context
import android.net.http.HttpException
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


fun Context.displayToast(
    message: String,
    apply: Toast.() -> Unit = {}
) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .apply {
            apply()
        }.show()
}

/** Converts 1500 → "1.5K", 0 → "0", 1000000 → "1M", etc. */
fun Int.toDisplayString(): String = when {
    this >= 1_000_000 -> "${"%.1f".format(this / 1_000_000.0)}M".trimEnd('0').trimEnd('.')
    this >= 1_000 -> "${"%.1f".format(this / 1_000.0)}K".trimEnd('0').trimEnd('.')
    else -> this.toString()
}

suspend inline fun <T> dispatchIo(crossinline job: suspend () -> T): T {
    return withContext(Dispatchers.IO) {
        job()
    }
}

suspend inline fun <T> safeApiCall(crossinline call: suspend () -> T?): Result<T> {
    return try {
        dispatchIo<Result<T>> {
            val result = call()

            if (result == null) {
                Result.failure<T>(Exception("Something unexpected happened."))
            }

            Result.success(result!!)
        }
    } catch (e: HttpException) {
        Timber.tag("safeApiCall").e(e)
        Result.failure(Exception("Network issue."))
    } catch (e: Exception) {
        Timber.tag("safeApiCall").e(e)
        Result.failure(e)
    }
}