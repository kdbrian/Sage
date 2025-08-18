package com.kdbrian.sage.util

import androidx.core.app.NotificationCompat

data class NotificationData(
    val channelId: String,
    val title: String,
    val message: String,
    val smallIcon: Int,
    val bigText: String? = null,
    val priority: Int = NotificationCompat.PRIORITY_DEFAULT,
    val autoCancel: Boolean = true
)
