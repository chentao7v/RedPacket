package me.chentao.redpacket.processor

import me.chentao.redpacket.utils.KVStore
import me.chentao.redpacket.utils.filterWordsList
import timber.log.Timber

/**
 * create by chentao on 2023-12-29.
 */
object Filter {

  /**
   * 过滤器是否包含 [text] 文本，包含则返回 true，否则返回 false
   */
  fun filter(text: String?): Boolean {
    if (text == null) {
      return false
    }

    val words = KVStore.filterWordsList()
    Timber.d("过滤器入参：$text，当前过滤器：$words")
    for (word in words) {
      if (word.contains(text)) {
        return true
      }
    }

    return false
  }

}