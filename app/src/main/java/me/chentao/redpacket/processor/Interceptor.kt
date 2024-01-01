package me.chentao.redpacket.processor

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * create by chentao on 2023-12-29.
 */
interface Interceptor {

  companion object {
    const val PLACEHOLDER = "[微信红包]"
    const val WECHAT_PACKAGE = "com.tencent.mm"

//    private const val FORMATTER = "yyyy-MM-dd hh:mm:ss.SSS"
//    private val dateFormatter = SimpleDateFormat(FORMATTER)
  }

  fun intercept(uiPage: UIPage, event: AccessibilityEvent, root: AccessibilityNodeInfo?): Boolean


  interface Chain {

    fun proceed(event: AccessibilityEvent, root: AccessibilityNodeInfo?)

    fun addInterceptor(interceptor: Interceptor)

  }

}