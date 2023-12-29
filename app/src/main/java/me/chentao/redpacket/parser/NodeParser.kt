package me.chentao.redpacket.parser

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * create by chentao on 2023-12-29.
 */
object NodeParser {

  fun findNodesById(event: AccessibilityEvent, viewId: String): List<AccessibilityNodeInfo>? {
    val nodeList = event.source?.findAccessibilityNodeInfosByViewId(viewId)
    if (nodeList.isNullOrEmpty()) {
      return null
    }
    return nodeList
  }

  fun findNodeById(event: AccessibilityEvent, viewId: String): AccessibilityNodeInfo? {
    val list = findNodesById(event, viewId)
    if (list.isNullOrEmpty()) {
      return null
    }
    return list[0]
  }

  fun getNodeText(event: AccessibilityEvent, viewId: String): String? {
    val node = findNodeById(event, viewId) ?: return null
    return node.text.toString()
  }

}