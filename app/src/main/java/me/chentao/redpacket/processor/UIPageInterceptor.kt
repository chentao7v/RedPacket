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

  companion object {

    const val DEFAULT_PAGE = "com.tencent.mm.ui.LauncherUI"

    /** 新红包，未拆开，红包展示页面，可以点击开；红包已被他人领取 */
    const val PACKET_SHOW_NOT_OPEN = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI"

    /** 进入红包详情页前的 UI */
    const val PACKET_OPENED_BEFORE_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyBeforeDetailUI"

    /** 红包详情页 UI */
    const val PACKET_OPENED_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI"

  }

  private var currentUI = DEFAULT_PAGE

  private val realScreenWidth = screenWidth

  override fun intercept(uiPage: UIPage, event: AccessibilityEvent, rootNode: AccessibilityNodeInfo): Boolean {
    if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      return false
    }

    val activityUI = event.getCurrentActivityName(appContext) ?: DEFAULT_PAGE
    currentUI = activityUI
    Timber.d("当前 UIPage -> $activityUI")

    return true
  }

  override fun currentUI(): String {
    return currentUI
  }

  override fun realScreenWidth(): Int {
    return this.realScreenWidth
  }

}