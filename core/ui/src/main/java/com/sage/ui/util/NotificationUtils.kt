package com.sage.ui.util

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationUtils {


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun Context.showNotification(data: NotificationData, notificationId: Int) {
        val builder = NotificationCompat.Builder(this, data.channelId).setSmallIcon(data.smallIcon)
            .setContentTitle(data.title).setContentText(data.message).setPriority(data.priority)
            .setAutoCancel(data.autoCancel)

        data.bigText?.let {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(it))
        }

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }


}