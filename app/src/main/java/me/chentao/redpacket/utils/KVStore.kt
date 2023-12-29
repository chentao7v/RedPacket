package me.chentao.redpacket.utils

/**
 * create by chentao on 2023-12-29.
 */
object KVStore {

  private const val NOTIFICATION = "notification_switch"

  private const val FILTER_WORDS = "filter_words"

  var notification: Boolean
    set(value) {
      SP.put(NOTIFICATION, value)
    }
    get() {
      return SP.getBool(NOTIFICATION)
    }

  var filterWords: String
    set(value) {
      SP.putApply(FILTER_WORDS, value)
    }
    get() {
      return SP.getString(FILTER_WORDS)
    }

  var notificationHint: Boolean = false
}