package me.chentao.redpacket.utils

import java.util.Collections

/**
 * create by chentao on 2023-12-29.
 */
object KVStore {

  private const val NOTIFICATION = "notification_switch"

  private const val FILTER_WORDS = "filter_words"

  private const val CONVERSATION_LIST = "conversation_list_switch"

  private const val OPEN_MYSELF = "open_myself"

  private const val FOREGROUND = "foreground"

  private const val REQUIRE_NEW_VERSION = "require_new_version"

  var notification: Boolean
    set(value) {
      SP.put(NOTIFICATION, value)
    }
    get() {
      return SP.getBool(NOTIFICATION)
    }

  var foreground: Boolean
    set(value) {
      SP.put(FOREGROUND, value)
    }
    get() {
      return SP.getBool(FOREGROUND)
    }

  var openMySelf: Boolean
    set(value) {
      SP.put(OPEN_MYSELF, value)
    }
    get() {
      return SP.getBool(OPEN_MYSELF)
    }

  var requireNewVersion: Boolean
    set(value) {
      SP.put(REQUIRE_NEW_VERSION, value)
    }
    get() {
      return SP.getBool(REQUIRE_NEW_VERSION)
    }

  var conversationList: Boolean
    set(value) {
      SP.put(CONVERSATION_LIST, value)
    }
    get() {
      return SP.getBool(CONVERSATION_LIST)
    }

  var filterWords: String
    set(value) {
      SP.putApply(FILTER_WORDS, value)
    }
    get() {
      return SP.getString(FILTER_WORDS)
    }
}

fun KVStore.filterWordsList(): List<String> {
  val words = filterWords
  if (words.isEmpty()) {
    return Collections.emptyList()
  }

  return words.split(",")
}