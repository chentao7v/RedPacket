package me.chentao.redpacket.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.view.accessibility.AccessibilityEvent
import me.chentao.redpacket.service.RedPacketService
import timber.log.Timber
import java.util.Date

/**
 * 无障碍工具扩展
 *
 * create by chentao on 2023-12-28.
 */

/**
 * 日志 TAG
 */
private const val TAG = "XXX"

fun AccessibilityEvent?.getCurrentActivityName(context: Context): String {
  val event = this ?: return ""

  val component = ComponentName(event.packageName.toString(), event.className.toString())
  try {
    var activityName = context.packageManager.getActivityInfo(component, 0).toString()
    activityName = activityName.substring(activityName.indexOf(" "), activityName.indexOf("}"))
    logWithTime("当前窗口activity:$activityName")
    return activityName
  } catch (e: PackageManager.NameNotFoundException) {
    logWithTime("getActivityName 异常：${e.message}")
    return ""
  }
}

fun logWithTime(msg: String) {
  val realMsg = msg + " -> " + RedPacketService.dateFormatter.format(Date())
  Timber.tag(TAG).d(realMsg)
}

/*

fun parseRedPacketInfo(event: AccessibilityEvent) {
  val amount = getRedPacketAmount(event) ?: return
  val from = getRedPacketFrom(event) ?: return
  val time = getRedPacketOpenTime(event, "moooorty")
}

fun getRedPacketFrom(event: AccessibilityEvent): String? {
  val nodeList = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j0j")
  if (nodeList.isNullOrEmpty()) {
    return null
  }

  val node = nodeList[0]
  // leo的红包
  val text = node.text
  val index = text.indexOf("的红包")
  val from = text.substring(0, index)
  logWithTime("红包来自：$from")
  return from
}

fun getRedPacketOpenTime(event: AccessibilityEvent, wechatNickname: String): String? {
  val nodeList = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j6p")
  if (nodeList.isNullOrEmpty()) {
    return null
  }

  val filter = nodeList.filter { node -> node.text.toString() == wechatNickname }
  if (filter.isEmpty()) {
    return null
  }

  val timeNodeList = filter[0].getParent().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j6q")
  if (timeNodeList.isNullOrEmpty()) {
    return null
  }
  val time = timeNodeList[0].text.toString()


  logWithTime("红包领取时间：$time")


  return time
}

fun getRedPacketAmount(event: AccessibilityEvent): Double? {
  val nodeList = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/iyw")
  if (nodeList.isNullOrEmpty()) {
    return null
  }

  val text = nodeList[0].text
  logWithTime("当前金额：$text")
  return text.toString().toDoubleOrNull()
}
 */