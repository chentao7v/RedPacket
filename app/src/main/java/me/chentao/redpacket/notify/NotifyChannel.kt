package me.chentao.redpacket.notify

import android.os.Build
import me.chentao.redpacket.utils.app

/**
 * create by chentao on 2024-01-14.
 */
data class NotifyChannel(
  val id: String,
  val name: String,
  val desc: String,
)

val foregroundChannel by lazy {
  NotifyChannel(
    id = "foreground", name = "前台服务", desc = "小助手为您保驾护航中"
  )
}

fun initChannelIfNecessary() {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    createChannel(app, foregroundChannel)
  }
}