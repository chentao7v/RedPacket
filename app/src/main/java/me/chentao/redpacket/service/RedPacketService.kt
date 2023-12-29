package me.chentao.redpacket.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import me.chentao.redpacket.utils.getCurrentActivityName
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date


/**
 * create by chentao on 2023-12-27.
 */
class RedPacketService : AccessibilityService() {

  companion object {
    private const val formatter = "yyyy-MM-dd hh:mm:ss.SSS"
    val dateFormatter = SimpleDateFormat(formatter)
    private const val TAG = "PacketRed"
  }

  override fun onServiceConnected() {

  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    event ?: return
    Timber.d("事件更新： -> $event")


    val currentActivityName = event.getCurrentActivityName(this)
    if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      // 页面切换时
      event.getCurrentActivityName(this)

    } else if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
      // 页面内容变化时
//      findNonGetRedPacket(event)
//      openRedPackage(event)
//      closeRedPackageUI(event)
    } else if (event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
      // 收到通知


    }
  }

  /**
   * 关闭红包页面
   */
  private fun closeRedPackageUI(event: AccessibilityEvent) {
    // 抢到红包后的关闭
    val backListGet = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/nnc")
    if (backListGet?.isNotEmpty() == true) {
      backListGet[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
      logWithTime("当前红包已经抢到了，关闭抢红包结果页面")
    }

    // 未抢到红包的关闭 --> 这里可能是红包未打开的，所以未打开的红包不能关闭
    val backListNoGet = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j6f")
    // 要看是否有 看看大家的手气
    val seeOthersNodes = event.source?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j6d")
    if (!seeOthersNodes.isNullOrEmpty() && backListNoGet?.isNotEmpty() == true) {
      // 有开看大家的手气，就说明红包抢完了
      backListNoGet[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
      logWithTime("很遗憾，当前红包没抢到，关闭抢红包页面")
    }
  }

  private fun logWithTime(msg: String) {
    val realMsg = msg + " -> " + dateFormatter.format(Date())
    Timber.tag(TAG).d(realMsg)
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
    logWithTime("打开红包，点击开按钮，开开开~~~~")
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
        logWithTime("当前红包未领取，点开红包，准备领取")
        redPackageRoot.performAction(AccessibilityNodeInfo.ACTION_CLICK)
      }
    }

  }

  override fun onInterrupt() {

  }

}