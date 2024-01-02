package me.chentao.redpacket.processor

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import me.chentao.redpacket.utils.appContext
import me.chentao.redpacket.utils.getCurrentActivityName
import me.chentao.redpacket.utils.screenWidth
import timber.log.Timber

/**
 * create by chentao on 2024-01-01.
 */
class UIPageInterceptor : Interceptor, UIPage {

  private var currentUI: String? = null

  private val realScreenWidth = screenWidth

  override fun intercept(uiPage: UIPage, event: AccessibilityEvent, root: AccessibilityNodeInfo?): Boolean {
    if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      return false
    }

    val activityUI = event.getCurrentActivityName(appContext)
    if (currentUI == null) {
      currentUI = activityUI
    } else if (activityUI != null) {
      currentUI = activityUI
    }
    Timber.d("当前 UIPage -> $activityUI")

    return true
  }

  override fun currentUI(): String {
    return currentUI ?: UIPage.DEFAULT_PAGE
  }

  override fun realScreenWidth(): Int {
    return this.realScreenWidth
  }

}