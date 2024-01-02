package me.chentao.redpacket.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.view.accessibility.AccessibilityEvent
import timber.log.Timber

/**
 * 无障碍工具扩展
 *
 * create by chentao on 2023-12-28.
 */

/**
 * 日志 TAG
 */

fun AccessibilityEvent?.getCurrentActivityName(context: Context): String? {
  val event = this ?: return ""

  val component = ComponentName(event.packageName.toString(), event.className.toString())
  return try {
    var activityName = context.packageManager.getActivityInfo(component, 0).toString()
    activityName = activityName.substring(activityName.indexOf(" "), activityName.indexOf("}"))
    Timber.d("当前窗口activity:$activityName")
    activityName
  } catch (e: PackageManager.NameNotFoundException) {
    Timber.d("getActivityName 异常：${e.message}")
    null
  }
}