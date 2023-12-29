package me.chentao.redpacket.processor

import android.view.accessibility.AccessibilityEvent

/**
 * create by chentao on 2023-12-29.
 */
class RealChain : Interceptor.Chain {

  private val interceptors = ArrayList<Interceptor>()

  override fun addInterceptor(interceptor: Interceptor) {
    interceptors.add(interceptor)
  }

  override fun proceed(event: AccessibilityEvent) {
    // 从前往后执行，如果前面的返回 false，则后面的也不执行
    for (interceptor in interceptors) {
      if (interceptor.intercept(event)) {
        return
      }
    }
  }
}