package com.kdbrian.sage.util

import android.content.Context
import android.widget.Toast


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