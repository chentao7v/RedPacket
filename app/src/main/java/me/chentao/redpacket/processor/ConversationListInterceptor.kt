package me.chentao.redpacket.processor

import android.view.accessibility.AccessibilityEvent
import me.chentao.redpacket.parser.NodeParser
import me.chentao.redpacket.parser.performClick
import me.chentao.redpacket.utils.KVStore
import timber.log.Timber

/**
 * 微信会话列表拦截器
 * <br>
 * create by chentao on 2023-12-30.
 */
class ConversationListInterceptor : Interceptor {

  companion object {
    private const val CONVERSATION_ROOT_ID = "com.tencent.mm:id/cj1"
    private const val CONVERSATION_UNREAD_COUNT = "com.tencent.mm:id/o_u"

  }

  override fun intercept(uiPage: UIPage, event: AccessibilityEvent): Boolean {
    if (!KVStore.conversationList || event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
      return false
    }

    val nodeList = NodeParser.findNodesById(event, CONVERSATION_ROOT_ID) ?: return false
    Timber.d("会话列表节点数量：${nodeList.size}")
    nodeList.forEach { node ->
      val unreadNode = NodeParser.findChildNodeById(node, CONVERSATION_UNREAD_COUNT)
      if (unreadNode != null) {
        val unreadCount = unreadNode.text?.toString()?.toIntOrNull()
        Timber.d("unreadCount -> $unreadCount")
        if (unreadCount != null) {
          node.performClick()
        }
      }
    }

    return true
  }
}