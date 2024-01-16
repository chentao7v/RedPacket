package me.chentao.redpacket.utils

import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * create by chentao on 2022-10-18.
 */
object JSONMaker {

  private val gson = Gson()

  fun <T> fromJson(json: String, clazz: Class<T>): T {
    return gson.fromJson(json, clazz)
  }

  fun <T> fromJson(json: String, type: Type): T {
    return gson.fromJson(json, type)
  }

  fun toJson(obj: Any): String {
    return gson.toJson(obj)
  }

}