package me.chentao.redpacket.processor

import me.chentao.redpacket.utils.KVStore
import timber.log.Timber

/**
 * create by chentao on 2023-12-29.
 */
object Filter {

  private val items: List<String>
    get() {
      return KVStore.filterWords.split(",")
    }

  fun filter(text: String): Boolean {
    Timber.d("过滤器入参：$text")
    return !items.contains(text)
  }

}