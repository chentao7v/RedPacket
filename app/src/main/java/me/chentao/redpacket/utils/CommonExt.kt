package me.chentao.redpacket.utils

import android.content.Context
import android.os.Handler
import android.os.Looper

/**
 * create by chentao on 2023-12-28.
 */
@SuppressWarnings("StaticFieldLeak")
lateinit var app: Context

private val handler = Handler(Looper.getMainLooper())

fun postDelay(delay: Long, block: () -> Unit) {
  handler.postDelayed({
    block()
  }, delay)
}

fun runOnUiThread(block: () -> Unit) {
  handler.post(block)
}

fun isUiThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

val Number.dp: Int
  get() {
    val dp = this.toInt()
    return (dp * app.resources.displayMetrics.density).toInt()
  }

val screenWidth: Int
  get() = app.resources.displayMetrics.widthPixels

val screenHeight: Int
  get() = app.resources.displayMetrics.heightPixels


val appVersionName: String by lazy {
  val packageInfo = app.packageManager.getPackageInfo(app.packageName, 0)
  packageInfo.versionName
}