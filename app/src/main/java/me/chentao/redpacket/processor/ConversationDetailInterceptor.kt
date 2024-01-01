package me.chentao.redpacket.processor

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import me.chentao.redpacket.parser.NodeParser
import me.chentao.redpacket.utils.KVStore
import timber.log.Timber

/**
 * 会话详情
 *
 * create by chentao on 2024-01-01.
 */
class ConversationDetailInterceptor : Interceptor {

  private companion object {
    private const val CHAR_TARGET_ID = "com.tencent.mm:id/obn"

  }

  override fun intercept(uiPage: UIPage, event: AccessibilityEvent): Boolean {
    if (event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
      return false
    }

    if (filterTargetUser(event)) {
      return true
    }



    return true
  }

  private fun filterTargetUser(event: AccessibilityEvent): Boolean {
    // 过滤器
    val filterWords = KVStore.filterWords
    val words = filterWords.split(",").toMutableList()
    // 过滤器未空，不过滤
    if (words.isEmpty()) {
      return false
    }

    val targetNode = NodeParser.findNodeById(event, CHAR_TARGET_ID) ?: return false
    val targetName = targetNode.text.toString()

    for (word in words) {
      // 目标名字中包含 过滤词 -> 过滤
      if (targetName.contains(word)) {
        return true
      }
    }
    return false
  }

  /**
   * 打开红包
   */
  private fun openRedPackage(event: AccessibilityEvent) {
    val openNodeList = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j6g")
    if (openNodeList.isNullOrEmpty()) {
      return
    }
    // 开红包
    openNodeList[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
    Timber.d("打开红包，点击开按钮，开开开~~~~")
  }


  /**
   * 关闭红包页面
   */
  private fun closeRedPackageUI(event: AccessibilityEvent) {
    // 抢到红包后的关闭
    val backListGet = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/nnc")
    if (backListGet?.isNotEmpty() == true) {
      backListGet[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
      Timber.d("当前红包已经抢到了，关闭抢红包结果页面")
    }

    // 未抢到红包的关闭 --> 这里可能是红包未打开的，所以未打开的红包不能关闭
    val backListNoGet = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j6f")
    // 要看是否有 看看大家的手气
    val seeOthersNodes = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j6d")
    if (!seeOthersNodes.isNullOrEmpty() && backListNoGet?.isNotEmpty() == true) {
      // 有开看大家的手气，就说明红包抢完了
      backListNoGet[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
      Timber.d("很遗憾，当前红包没抢到，关闭抢红包页面")
    }
  }

  /**
   * 找到未被领取的红包并点击
   */
  private fun findNonGetRedPacket(event: AccessibilityEvent) {
    // 微信红包节点
    val packetNodeList = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a3y")
    if (packetNodeList.isNullOrEmpty()) {
      return
    }

    // 消息列表可能有多个微信红包，多个已领取未领取的
    packetNodeList.forEach { node ->
      val redPackageRoot = node.parent
      if (redPackageRoot == null) {
        return
      }

      // 已领取/已被领完
      val nodesOfGet = redPackageRoot.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a3m")
      // 不包含 已领取/已被领完 -> 未领取
      if (nodesOfGet.isNullOrEmpty()) {
        Timber.d("当前红包未领取，点开红包，准备领取")
        redPackageRoot.performAction(AccessibilityNodeInfo.ACTION_CLICK)
      }
    }

  }

}