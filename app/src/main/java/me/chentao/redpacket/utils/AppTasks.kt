package me.chentao.redpacket.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

/**
 * create by chentao on 2024-01-15.
 */
fun hideFromRecentTasks(context: Context, hide: Boolean) {
  val systemService = context.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
  val appTasks = systemService.appTasks
  val size = appTasks.size

  if (size > 0) {
    // 设置activity是否隐藏
    appTasks[0].setExcludeFromRecents(hide)
  }
}

fun toLauncher(context: Context) {
  val intent = Intent(Intent.ACTION_MAIN)
  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  intent.addCategory(Intent.CATEGORY_HOME)
  context.startActivity(intent)
}

fun toAppSettings(context: Context) {
  val intent = Intent()
  intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
  val uri = Uri.fromParts("package", context.packageName, null)
  intent.setData(uri)
  context.startActivity(intent)
}

