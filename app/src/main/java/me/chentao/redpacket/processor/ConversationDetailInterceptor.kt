package me.chentao.redpacket.processor

import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import me.chentao.redpacket.bean.ChatRedPacketMsg
import me.chentao.redpacket.parser.NodeParser
import me.chentao.redpacket.parser.performClick
import me.chentao.redpacket.utils.KVStore
import timber.log.Timber
import java.util.Collections

/**
 * 会话详情
 *
 * create by chentao on 2024-01-01.
 */
class ConversationDetailInterceptor : Interceptor {

  private companion object {

    /// 消息列表
    private const val CHAT_TARGET_ID = "com.tencent.mm:id/obn"
    private const val CHAT_MSG_ROOT_ID = "com.tencent.mm:id/bn1"
    private const val CHAT_RED_PACKET_DESC_ID = "com.tencent.mm:id/a3y"
    private const val CHAT_RED_PACKET_ROOT = "com.tencent.mm:id/bkg"
    private const val CHAT_AVATAR_ID = "com.tencent.mm:id/bk1"
    private const val CHAT_RED_PACKET_STATUS_ID = "com.tencent.mm:id/a3m"
    private const val CHAT_GROUP_MSG_TARGET_ID = "com.tencent.mm:id/brc"

    /// 红包相关
    private const val RED_PACKET_DIALOG_OPEN_ID = "com.tencent.mm:id/j6g"
    private const val RED_PACKET_DIALOG_CLOSE_ID = "com.tencent.mm:id/j6f"
    private const val RED_PACKET_DETAIL_BACK_ID = "com.tencent.mm:id/nnc"
    private const val RED_PACKET_DIALOG_HINT_ID = "com.tencent.mm:id/j6c"

    /// 红包提示
    private const val RED_PACKET_HINT_TOO_SLOW = "手慢了"
    private const val RED_PACKET_HINT_OUT_OF_DATE = "过期"
  }

  override fun intercept(uiPage: UIPage, event: AccessibilityEvent, root: AccessibilityNodeInfo?): Boolean {
    if (event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
      return false
    }

    val targetName = getTargetName(root, event)
    Timber.d("target 目标会话：$targetName")

    if (Filter.filter(targetName)) {
      Timber.d("过滤掉与 [$targetName] 的会话，不处理内部红包")
      return true
    }

    // 1.找到未拆封的红包
    val redPackets = findUnOpenRedPackets(uiPage, event)
    // 2.点击红包
    clickRedPackets(redPackets)
    // 3.拆开红包
    unboxRedPackets(uiPage, event)
    // 4.关闭拆红包页面
    finishRedPacketUI(uiPage, event)
    return true
  }

  private fun finishRedPacketUI(uiPage: UIPage, event: AccessibilityEvent) {
    val currentUI = uiPage.currentUI()
    val detailBackNode = NodeParser.findNodeById(event, RED_PACKET_DETAIL_BACK_ID)
    val dialogCloseNode = NodeParser.findNodeById(event, RED_PACKET_DIALOG_CLOSE_ID)
    val dialogHintNode = NodeParser.findNodeById(event, RED_PACKET_DIALOG_HINT_ID)

    if (currentUI.contains(UIPage.PACKET_OPENED_DETAIL) && detailBackNode != null) {
      Timber.d("红包详情页显示了，执行关闭操作")
      // 红包详情页
      detailBackNode.performClick()
    } else if (currentUI.contains(UIPage.PACKET_SHOW_NOT_OPEN) && dialogCloseNode != null && dialogHintNode != null) {
      // 红包未拆开页面，但是红包状态不正确。如红包已过期、红包已派完、红包已被领取等
      // 注意：这里要避免错误关闭红包未拆开的页面
      val dialogHint = dialogHintNode.text?.toString()
      Timber.d("红包页面，当前红包状态文字：$dialogHint")
      if (dialogHint == null) {
        return
      }

      if (dialogHint.contains(RED_PACKET_HINT_TOO_SLOW) || dialogHint.contains(RED_PACKET_HINT_OUT_OF_DATE)) {
        dialogCloseNode.performClick()
        Timber.d("关闭无效红包页面")
      }

    }
  }

  /**
   * 拆开红包
   */
  private fun unboxRedPackets(uiPage: UIPage, event: AccessibilityEvent) {
    Timber.d("准备拆红包，当前页面：${uiPage.currentUI()}")
    if (!uiPage.currentUI().contains(UIPage.PACKET_SHOW_NOT_OPEN)) {
      return
    }

    Timber.d("当前到了开红包页面啦")
    val node = NodeParser.findNodeById(event, RED_PACKET_DIALOG_OPEN_ID) ?: return

    node.performClick()
    Timber.d("开开开、开红包咯~~~~")
  }

  private fun getTargetName(root: AccessibilityNodeInfo?, event: AccessibilityEvent): String? {
    val targetNode = if (root == null) {
      NodeParser.findNodeById(event, CHAT_TARGET_ID)
    } else {
      NodeParser.findChildNodeById(root, CHAT_TARGET_ID)
    }

    return targetNode?.text?.toString()
  }

  private fun clickRedPackets(redPackets: List<ChatRedPacketMsg>) {
    for (redPacketMsg in redPackets) {
      // 是否拆开我的红包
      if (redPacketMsg.mine && !KVStore.openMySelf) {
        Timber.d("配置的不拆开我自己的红包")
        continue
      }

      // 考虑过滤红包
      // 针对的是 不过滤群红包，但是过滤指定用户发的红包
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

}