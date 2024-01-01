package me.chentao.redpacket.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import me.chentao.redpacket.processor.ConversationDetailInterceptor
import me.chentao.redpacket.processor.ConversationListInterceptor
import me.chentao.redpacket.processor.Interceptor
import me.chentao.redpacket.processor.Interceptor.Companion.WECHAT_PACKAGE
import me.chentao.redpacket.processor.NotificationInterceptor
import me.chentao.redpacket.processor.RealChain
import me.chentao.redpacket.processor.UIPageInterceptor
import timber.log.Timber
import java.text.SimpleDateFormat


/**
 * create by chentao on 2023-12-27.
 */
class RedPacketService : AccessibilityService() {

  companion object {
    private const val formatter = "yyyy-MM-dd hh:mm:ss.SSS"
    val dateFormatter = SimpleDateFormat(formatter)
    private const val TAG = "PRD"
  }

  private lateinit var chain: Interceptor.Chain

  override fun onServiceConnected() {
    val uiPage = UIPageInterceptor()

    chain = RealChain(uiPage)
    chain.addInterceptor(uiPage)
    chain.addInterceptor(NotificationInterceptor())
    chain.addInterceptor(ConversationListInterceptor())
    chain.addInterceptor(ConversationDetailInterceptor())
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    event ?: return
    if (!event.packageName.equals(WECHAT_PACKAGE)) {
      return
    }

    Timber.d("事件更新： -> $event")
    chain.proceed(event)
  }


  override fun onInterrupt() {

  }

}