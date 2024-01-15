package me.chentao.redpacket.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import me.chentao.redpacket.notify.foregroundChannel
import me.chentao.redpacket.notify.foregroundNotify
import me.chentao.redpacket.notify.judgeNotificationPermission
import me.chentao.redpacket.notify.startForegroundWithNotify
import me.chentao.redpacket.processor.ConversationDetailInterceptor
import me.chentao.redpacket.processor.ConversationListInterceptor
import me.chentao.redpacket.processor.Interceptor
import me.chentao.redpacket.processor.Interceptor.Companion.WECHAT_PACKAGE
import me.chentao.redpacket.processor.NotificationInterceptor
import me.chentao.redpacket.processor.RealChain
import me.chentao.redpacket.processor.UIPageInterceptor
import me.chentao.redpacket.utils.KVStore
import timber.log.Timber


/**
 * create by chentao on 2023-12-27.
 */
class RedPacketService : AccessibilityService() {


  companion object {

    private const val EXTRA_FOREGROUND = "extra_foreground"

    fun startForeground(context: Context, open: Boolean) {
      val intent = Intent(context, RedPacketService::class.java)
      intent.putExtra(EXTRA_FOREGROUND, open)
      context.startService(intent)
    }

  }

  private lateinit var chain: Interceptor.Chain

  private val wm by lazy { getSystemService(WINDOW_SERVICE) as WindowManager }
  private var aliveView: View? = null

  override fun onCreate() {
    super.onCreate()
    Timber.e("onCreate --> service")
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Timber.e("onStartCommand --> service --> ${intent?.getBooleanExtra(EXTRA_FOREGROUND, false)}")

    val openFlag = intent?.getBooleanExtra(EXTRA_FOREGROUND, false) ?: false
    if (openFlag) {
      val judgeNotificationPermission = judgeNotificationPermission(foregroundChannel.id)
      // 判断权限，并且用户勾选了前台服务
      if (judgeNotificationPermission && KVStore.foreground) {
        startForegroundWithNotify(this, foregroundChannel.id, foregroundNotify)
      }
    } else {
      stopForeground(STOP_FOREGROUND_REMOVE)
    }

    return super.onStartCommand(intent, flags, startId)
  }

  override fun onServiceConnected() {
    createTempView()

    val uiPage = UIPageInterceptor()

    chain = RealChain(uiPage)
    chain.addInterceptor(uiPage)
    chain.addInterceptor(NotificationInterceptor())
    chain.addInterceptor(ConversationListInterceptor())
    chain.addInterceptor(ConversationDetailInterceptor())
  }

  /**
   * 兼容小米，无障碍服务无法在后台运行
   */
  private fun createTempView() {
    if (aliveView != null) {
      wm.removeView(aliveView)
    }

    val tempView = View(this)
    val lp = WindowManager.LayoutParams().apply {
      type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
      format = PixelFormat.TRANSLUCENT
      flags = flags or
          WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
          WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
          WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
          WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
      width = 1
      height = 1
    }
    try {
      wm.addView(tempView, lp)
      aliveView = tempView
    } catch (e: Exception) {
      Timber.e("创建无障碍悬浮框失败")
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
    Timber.e("service --> interrupted")
  }

  override fun onDestroy() {
    super.onDestroy()
    Timber.e("onDestroy --> service")

    if (aliveView != null) {
      wm.removeView(aliveView)
    }
  }
}