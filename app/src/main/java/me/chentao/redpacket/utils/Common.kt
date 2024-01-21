package me.chentao.redpacket.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.StringRes
import me.chentao.redpacket.R
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.File

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


val packageInfo by lazy {
  app.packageManager.getPackageInfo(app.packageName, 0)
}

val appVersionName: String by lazy {
  packageInfo.versionName
}

val appVersionCode: Int by lazy {
  packageInfo.versionCode
}


val appName: String by lazy {
  getStringRes(R.string.app_name)
}

fun getStringRes(@StringRes id: Int): String = app.resources.getString(id)

fun getStringRes(@StringRes id: Int, vararg formatArgs: Any?): String {
  return app.resources.getString(id, *formatArgs)
}

fun CharSequence.copyToClipboard(label: String = appName) {
  val manager = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  val clipData = ClipData.newPlainText(label, this)
  manager.setPrimaryClip(clipData)
}

fun getDownloadDirectory(): File? {
  val file = app.getExternalFilesDir("downloads")
  if (file != null && !file.exists()) {
    file.mkdirs()
  }
  return file
}

fun String.toHttps(): String {
  val url = this
  val httpUrl = url.toHttpUrlOrNull()
  if (httpUrl != null) {
    // 确保使用 https 访问图片
    return httpUrl.newBuilder()
      .scheme("https")
      .build()
      .toString()
  } else {
    return url
  }
}