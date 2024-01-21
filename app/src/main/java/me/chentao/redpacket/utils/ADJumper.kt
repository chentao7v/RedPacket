package me.chentao.redpacket.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(app.packageManager) != null) {
          context.startActivity(intent)
        } else {
          showToast(getStringRes(R.string.contact_us_when_ad_error))
        }
      }
    }
  }

}