package me.chentao.redpacket.bean

import android.view.accessibility.AccessibilityNodeInfo

/**
 * create by chentao on 2024-01-01.
 */
data class ChatRedPacketMsg(
  /** 是否是我发出的红包 */
  val mine: Boolean,
  val target: String?,
  val node: AccessibilityNodeInfo,
)