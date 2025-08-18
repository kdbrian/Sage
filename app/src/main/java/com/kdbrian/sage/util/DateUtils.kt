package com.kdbrian.sage.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {


    fun Long.formatAsDate(pattern: String = MONTH_DAY): String {
        val date = Date(this)
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(date)
    }


    const val DEFAULT = "yyyy-MM-dd HH:mm:ss"
    const val DATE_ONLY = "yyyy-MM-dd"
    const val TIME_ONLY = "HH:mm:ss"
    const val READABLE_DATE = "dd MMM yyyy"
    const val READABLE_DATE_TIME = "dd MMM yyyy, HH:mm"
    const val ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val US_DATE = "MM/dd/yyyy"
    const val EU_DATE = "dd/MM/yyyy"
    const val SHORT_DATE = "dd/MM/yy"
    const val MONTH_DAY = "MMM dd"
    const val FULL = "EEEE, dd MMMM yyyy HH:mm:ss"


}