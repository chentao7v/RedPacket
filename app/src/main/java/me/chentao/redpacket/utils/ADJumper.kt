package me.chentao.redpacket.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import me.chentao.redpacket.R
import me.chentao.redpacket.data.bean.ADItem
import me.chentao.redpacket.ui.RichTextActivity

/**
 * create by chentao on 2024-01-21.
 */
object ADJumper {


  fun launch(context: Context, item: ADItem) {
    val navType = item.navType
    val title = item.title ?: ""
    val url = item.navUrl ?: ""
    val content = item.content ?: ""

    when (navType) {
      ADItem.NAV_APP_INTERNAL -> {
        RichTextActivity.launch(context, title, content)
      }

      ADItem.NAV_WEB -> {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(app.packageManager) != null) {
          context.startActivity(intent)
        } else {
          showToast(getStringRes(R.string.input_chrome_first))
        }
      }

      ADItem.NAV_THIRD_APP -> {
        launchUri(context, url)
      }
    }
  }

  private fun launchUri(context: Context, url: String) {
    // https://github.com/Oct1a/TikTok-Scheme
    var newUri = Uri.parse(url)
    val appName: String = when (val schema = newUri.scheme) {
      // 抖音
      "snssdk1128" -> {
        if (!resolveUri(newUri)) {
          // 抖音极速版
          val dyJS = url.replace("snssdk1128://", "snssdk2329://").toUri()
          // 抖音火山版
          val dyHS = url.replace("snssdk1128://", "snssdk1112://").toUri()
          if (resolveUri(dyJS)) {
            newUri = dyJS
          } else if (resolveUri(dyHS)) {
            newUri = dyHS
          }
        }

        "抖音"
      }

      // 快手
      "kwai" -> {
        "快手"
      }

      "snssdk32" -> {
        "西瓜视频"
      }

      "weishi" -> {
        "微视"
      }

      else -> {
        "与${schema}对应App"
      }
    }

    val intent = Intent(Intent.ACTION_VIEW, newUri)
    if (intent.resolveActivity(app.packageManager) != null) {
      context.startActivity(intent)
    } else {
      showToast(getStringRes(R.string.contact_us_when_ad_error, appName))
    }
  }

  private fun resolveUri(uri: Uri): Boolean {
    val intent = Intent(Intent.ACTION_VIEW, uri)
    return intent.resolveActivity(app.packageManager) != null
  }

}