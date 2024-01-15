package me.chentao.redpacket.utils

import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * create by chentao on 2023-11-22.
 */

fun File.toSafeUri(): Uri {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    FileProvider.getUriForFile(app, app.packageName + ".provider", this)
  } else {
    Uri.fromFile(this)
  }
}