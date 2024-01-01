package me.chentao.redpacket.processor

import android.app.Notification
import android.view.accessibility.AccessibilityEvent
import me.chentao.redpacket.processor.Interceptor.Companion.PLACEHOLDER
import me.chentao.redpacket.utils.KVStore
import timber.log.Timber

/**
 * create by chentao on 2023-12-29.
 */
class NotificationInterceptor : Interceptor {

  override fun intercept(uiPage: UIPage, event: AccessibilityEvent): Boolean {
    if (event.eventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
      return false
    }

    val flag = KVStore.notification
    Timber.d("收到了微信消息通知，当前红包通知监听是否打开：$flag")

    if (!flag) {
      return false
    }

    // 处理通知点击
    val message = event.text.toString()
    Timber.d("通知内容：$message")
    // 推送消息包含 [微信红包] 字样
    if (!message.contains(PLACEHOLDER)) {
      return true
    }

    val parcelable = event.parcelableData
    val notification = (parcelable as? Notification) ?: return true

    // 通知标题：用户昵称/群组名
    val notificationTitle = notification.extras.getString("android.title") ?: ""
    Timber.d("通知标题：$notificationTitle")

    // 过滤掉不需要的群或者用户消息
    if (Filter.filter(notificationTitle)) {
      return true
    }

    notification.contentIntent?.send()
    return true
  }

}