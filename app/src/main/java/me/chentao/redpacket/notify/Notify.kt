package me.chentao.redpacket.notify

import me.chentao.redpacket.R

/**
 * create by chentao on 2024-01-15.
 */
data class Notify(
  val id: Int,
  val icon: Int = R.drawable.ic_robot_24dp,
  val title: String,
  val text: String,
  val ongoing: Boolean,
  val autoCancel: Boolean
)

val foregroundNotify by lazy {
  Notify(
    id = 1000,
    title = "让红包飞",
    text = "小助手正在为您保驾护航中",
    ongoing = true,
    autoCancel = false
  )
}
