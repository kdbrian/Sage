package com.sage.ui.util

import android.content.Context
import android.widget.Toast

object Utils {

    fun Context.displayToast(
        message: String,
        apply: Toast.() -> Unit = {}
    ) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .apply {
                apply()
            }.show()
    }
}