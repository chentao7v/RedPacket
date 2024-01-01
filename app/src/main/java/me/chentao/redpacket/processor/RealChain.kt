package me.chentao.redpacket.processor

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * create by chentao on 2023-12-29.
 */
class RealChain(private val uiPage: UIPage) : Interceptor.Chain {

  private val interceptors = ArrayList<Interceptor>()

  override fun addInterceptor(interceptor: Interceptor) {
    interceptors.add(interceptor)
  }

  override fun proceed(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo) {
    // 从前往后执行，如果前面的返回 false，则后面的也不执行
    for (interceptor in interceptors) {
      if (interceptor.intercept(uiPage, event, rootNode)) {
        return
      }
    }
  }
}