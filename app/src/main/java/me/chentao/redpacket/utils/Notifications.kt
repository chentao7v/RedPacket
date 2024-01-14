package me.chentao.redpacket.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import me.chentao.redpacket.bean.NotifyChannel

/**
 * create by chentao on 2024-01-14.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun createChannel(context: Context, notifyChannel: NotifyChannel) {
  val importance = NotificationManager.IMPORTANCE_LOW
  val channel = NotificationChannel(notifyChannel.id, notifyChannel.name, importance)
  channel.description = notifyChannel.desc
  val notificationManager = NotificationManagerCompat.from(context)
  notificationManager.createNotificationChannel(channel)
}



