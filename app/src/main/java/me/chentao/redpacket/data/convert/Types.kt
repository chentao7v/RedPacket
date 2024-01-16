package me.chentao.redpacket.data.convert

import com.google.gson.reflect.TypeToken
import me.chentao.redpacket.data.bean.PgyerResponse
import java.lang.reflect.Type

/**
 * create by chentao on 2024-01-16.
 */
object Types {

  fun <R> ofPgyer(clazz: Class<R>): Type {
    return TypeToken.getParameterized(PgyerResponse::class.java, clazz).type
  }


}