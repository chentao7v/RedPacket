package me.chentao.redpacket.notify

import android.app.Notification
import android.app.Notification.EXTRA_CHANNEL_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.EXTRA_APP_PACKAGE
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import me.chentao.redpacket.R
import me.chentao.redpacket.ui.MainActivity
import me.chentao.redpacket.utils.app
import me.chentao.redpacket.utils.toAppSettings


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

fun createNotification(context: Context, channelId: String, notify: Notify): Notification {

  val intent = Intent(context, MainActivity::class.java)
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

  val pendingIntent = PendingIntent.getActivity(
    context, notify.id, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
  )

  val builder = NotificationCompat.Builder(context, channelId)
    .setSmallIcon(notify.icon)
    .setContentTitle(notify.title)
    .setContentText(notify.text)
    .setContentIntent(pendingIntent)
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setOngoing(notify.ongoing)
    .setAutoCancel(notify.autoCancel)

  return builder.build()
}

fun startForegroundWithNotify(context: Service, channelId: String, notify: Notify) {
  val notification = createNotification(context, channelId, notify)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    context.startForeground(notify.id, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST)
  } else {
    context.startForeground(notify.id, notification)
  }
}

private fun isNotificationOpen(): Boolean {
  return NotificationManagerCompat.from(app).areNotificationsEnabled()
}

@RequiresApi(Build.VERSION_CODES.O)
private fun isNotificationChannelOpen(channelId: String): Boolean {
  val manager = app.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
  val channel = manager.getNotificationChannel(channelId)
  return channel.importance != NotificationManager.IMPORTANCE_NONE
}

fun judgeNotificationPermission(channelId: String): Boolean {
  val notificationOpen = isNotificationOpen()
  val notificationChannelOpen = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    isNotificationChannelOpen(channelId)
  } else {
    true
  }
  return notificationOpen && notificationChannelOpen
}

fun gotoNotifySettings(context: Context, channelId: String) {
  try {
    // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
    val intent = Intent()
    //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
      intent.putExtra(EXTRA_APP_PACKAGE, context.packageName)
      intent.putExtra(EXTRA_CHANNEL_ID, channelId)
    }
    //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
    intent.putExtra("app_package", context.packageName)
    intent.putExtra("app_uid", context.applicationInfo.uid)
    context.startActivity(intent)
  } catch (e: Exception) {
    toAppSettings(context)
  }
}


