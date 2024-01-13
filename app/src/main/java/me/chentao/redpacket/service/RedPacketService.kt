package me.chentao.redpacket.service

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import me.chentao.redpacket.processor.ConversationDetailInterceptor
import me.chentao.redpacket.processor.ConversationListInterceptor
import me.chentao.redpacket.processor.Interceptor
import me.chentao.redpacket.processor.Interceptor.Companion.WECHAT_PACKAGE
import me.chentao.redpacket.processor.NotificationInterceptor
import me.chentao.redpacket.processor.RealChain
import me.chentao.redpacket.processor.UIPageInterceptor
import timber.log.Timber


/**
 * create by chentao on 2023-12-27.
 */
class RedPacketService : AccessibilityService() {


  private lateinit var chain: Interceptor.Chain

  private val wm by lazy { getSystemService(WINDOW_SERVICE) as WindowManager }
  private var aliveView: View? = null

  override fun onServiceConnected() {

    createAccessibilityView()


    val uiPage = UIPageInterceptor()

    chain = RealChain(uiPage)
    chain.addInterceptor(uiPage)
    chain.addInterceptor(NotificationInterceptor())
    chain.addInterceptor(ConversationListInterceptor())
    chain.addInterceptor(ConversationDetailInterceptor())
  }

  private fun createAccessibilityView() {
    if (aliveView != null) {
      wm.removeView(aliveView)
    }

    val tempView = View(this)
    val lp = WindowManager.LayoutParams().apply {
      type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
      format = PixelFormat.TRANSLUCENT
      flags =
        flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
      width = 1
      height = 1
    }
    try {
      wm.addView(tempView, lp)
      aliveView = tempView
    } catch (e: Exception) {
      Timber.d("创建无障碍悬浮框失败")
    }
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    event ?: return
    val packageName = event.packageName ?: return
    if (packageName != WECHAT_PACKAGE) {
      return
    }

    Timber.d("事件更新： -> $event")
    val root: AccessibilityNodeInfo? = rootInActiveWindow
    Timber.d("root : $root")
    chain.proceed(event, root)
  }


  override fun onInterrupt() {

  }

  override fun onDestroy() {
    super.onDestroy()

    if (aliveView != null) {
      wm.removeView(aliveView)
    }
  }

}