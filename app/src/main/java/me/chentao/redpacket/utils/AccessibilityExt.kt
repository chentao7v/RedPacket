package me.chentao.redpacket.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
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

/**
 * 解析 Activity
 */
fun AccessibilityEvent?.getCurrentActivityName(context: Context): String? {
  val event = this ?: return ""

  val component = ComponentName(event.packageName.toString(), event.className.toString())
  val intent = Intent()
  intent.setComponent(component)
  val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
  Timber.d("getActivityName resolved result -> $resolveInfo")
  return resolveInfo?.activityInfo?.name
}