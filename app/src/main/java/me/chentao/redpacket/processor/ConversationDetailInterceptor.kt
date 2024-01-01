package me.chentao.redpacket.processor

import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import me.chentao.redpacket.bean.ChatRedPacketMsg
import me.chentao.redpacket.parser.NodeParser
import me.chentao.redpacket.parser.performClick
import timber.log.Timber
import java.util.Collections

/**
 * 会话详情
 *
 * create by chentao on 2024-01-01.
 */
class ConversationDetailInterceptor : Interceptor {

  private companion object {
    private const val CHAT_TARGET_ID = "com.tencent.mm:id/obn"
    private const val CHAT_MSG_ROOT_ID = "com.tencent.mm:id/bn1"
    private const val CHAT_RED_PACKET_DESC_ID = "com.tencent.mm:id/a3y"
    private const val CHAT_RED_PACKET_ROOT = "com.tencent.mm:id/bkg"
    private const val CHAT_AVATAR_ID = "com.tencent.mm:id/bk1"
    private const val CHAT_RED_PACKET_STATUS_ID = "com.tencent.mm:id/a3m"
    private const val CHAT_GROUP_MSG_TARGET_ID = "com.tencent.mm:id/brc"
  }

  override fun intercept(uiPage: UIPage, event: AccessibilityEvent): Boolean {
    if (event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
      return false
    }

    val targetNode = NodeParser.findNodeById(event, CHAT_TARGET_ID)
    val targetName = targetNode?.text?.toString()

    if (Filter.filter(targetName)) {
      Timber.d("过滤掉与 [$targetName] 的会话，不处理内部红包")
      return true
    }

    val redPackets = findUnOpenRedPackets(uiPage, event)
    openReadPackets(redPackets)

    return true
  }

  private fun openReadPackets(redPackets: List<ChatRedPacketMsg>) {
    for (redPacketMsg in redPackets) {
      // 是否拆开我的红包

      // 考虑过滤红包
      // 针对的是不过滤群红包，但是过滤指定用户发的红包
      val target = redPacketMsg.target
      if (Filter.filter(target)) {
        Timber.d("过滤掉红包，不点击。$redPacketMsg")
        continue
      }

      // 拆开红包
      Timber.d("点击红包 -> $redPacketMsg")
      redPacketMsg.node.performClick()
    }
  }

  /**
   * 找到未拆开的红包
   */
  private fun findUnOpenRedPackets(uiPage: UIPage, event: AccessibilityEvent): List<ChatRedPacketMsg> {
    val nodes = NodeParser.findNodesById(event, CHAT_MSG_ROOT_ID)
    if (nodes.isNullOrEmpty()) {
      return Collections.emptyList()
    }

    // 有聊天消息

    val redPacketMsgList = ArrayList<ChatRedPacketMsg>()

    nodes.forEach { chatNode ->
      val redPacketDescNode = NodeParser.findChildNodeById(chatNode, CHAT_RED_PACKET_DESC_ID)
      val avatarNode = NodeParser.findChildNodeById(chatNode, CHAT_AVATAR_ID)
      val redPacketStatusNode = NodeParser.findChildNodeById(chatNode, CHAT_RED_PACKET_STATUS_ID)
      val redPacketRootNode = NodeParser.findChildNodeById(chatNode, CHAT_RED_PACKET_ROOT)
      val groupMsgTargetNode = NodeParser.findChildNodeById(chatNode, CHAT_GROUP_MSG_TARGET_ID)

      if (redPacketDescNode != null && redPacketStatusNode == null && avatarNode != null && redPacketRootNode != null) {
        // 有红包，且红包未领，并且有头像节点
        // 判定红包是谁发的
        val rect = Rect()
        avatarNode.getBoundsInScreen(rect)
        val width = uiPage.realScreenWidth()
        // 如果头像的左边小于 1/2 的宽处
        // 判定是别人发的红包
        val others = rect.left < width / 2f

        val target = groupMsgTargetNode?.text?.toString()
        val msg = ChatRedPacketMsg(!others, target, redPacketRootNode)
        Timber.d("新增未领取红包消息 $msg")
        redPacketMsgList.add(msg)
      }
    }

    return redPacketMsgList
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