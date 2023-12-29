package me.chentao.redpacket.utils

/**
 * create by chentao on 2023-12-29.
 */
object KVStore {

  private const val NOTIFICATION = "notification_switch"

  var notification: Boolean
    set(value) {
      SP.put(NOTIFICATION, value)
    }
    get() {
      return SP.getBool(NOTIFICATION)
    }

}