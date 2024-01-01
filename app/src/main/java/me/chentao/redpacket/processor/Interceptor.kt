package me.chentao.redpacket.processor

import android.view.accessibility.AccessibilityEvent
import java.text.SimpleDateFormat
import java.util.Date

/**
 * create by chentao on 2023-12-29.
 */
interface Interceptor {

  companion object {
    const val PLACEHOLDER = "[微信红包]"
    const val WECHAT_PACKAGE = "com.tencent.mm"

    private const val FORMATTER = "yyyy-MM-dd hh:mm:ss.SSS"
    private val dateFormatter = SimpleDateFormat(FORMATTER)

    val currentTime: String
      get() {
        return dateFormatter.format(Date())
      }

  }

  fun intercept(event: AccessibilityEvent): Boolean


  interface Chain {

    fun proceed(event: AccessibilityEvent)

    fun addInterceptor(interceptor: Interceptor)

  }

}