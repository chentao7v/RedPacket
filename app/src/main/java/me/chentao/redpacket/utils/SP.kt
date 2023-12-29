package me.chentao.redpacket.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * create by chentao on 2023-12-29.
 */
object SP {

  private const val KEY_SP = "red_packet"

  private val core: SharedPreferences
    get() {
      return appContext.getSharedPreferences(KEY_SP, Context.MODE_PRIVATE)
    }

  fun put(key: String, value: Boolean) {
    core.edit()
      .putBoolean(key, value)
      .commit()
  }

  fun put(key: String, value: Int) {
    core.edit()
      .putInt(key, value)
      .commit()
  }

  fun put(key: String, value: String) {
    core.edit()
      .putString(key, value)
      .commit()
  }

  fun putApply(key: String, value: String) {
    core.edit()
      .putString(key, value)
      .apply()
  }

  fun put(key: String, value: Float) {
    core.edit()
      .putFloat(key, value)
      .commit()
  }

  fun getBool(key: String, defaultVal: Boolean = false): Boolean {
    return core.getBoolean(key, defaultVal)
  }

  fun getString(key: String, defaultVal: String = ""): String {
    return core.getString(key, defaultVal) ?: ""
  }

  fun getInt(key: String, defaultVal: Int = 0): Int {
    return core.getInt(key, defaultVal)
  }

  fun getFloat(key: String, defaultVal: Float = 0f): Float {
    return core.getFloat(key, defaultVal)
  }

}

