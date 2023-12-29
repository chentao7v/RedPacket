package me.chentao.redpacket.utils

import android.content.Context
import android.os.Handler
import android.os.Looper

/**
 * create by chentao on 2023-12-28.
 */

lateinit var appContext: Context
private val handler = Handler(Looper.getMainLooper())

fun postDelay(delay: Long, block: () -> Unit) {
  handler.postDelayed({
    block()
  }, delay)
}

val Number.dp: Int
  get() {
    val dp = this.toInt()
    return (dp * appContext.resources.displayMetrics.density).toInt()
  }