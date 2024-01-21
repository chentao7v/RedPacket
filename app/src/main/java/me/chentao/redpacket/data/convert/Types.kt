package me.chentao.redpacket.data.convert

import com.google.gson.reflect.TypeToken
import me.chentao.redpacket.data.bean.BaseResponse
import me.chentao.redpacket.data.bean.DataListResponse
import me.chentao.redpacket.data.bean.DataResponse
import me.chentao.redpacket.data.bean.PgyerResponse
import java.lang.reflect.Type

/**
 * create by chentao on 2024-01-16.
 */
object Types {

  fun <R> ofPgyer(clazz: Class<R>): Type {
    return TypeToken.getParameterized(PgyerResponse::class.java, clazz).type
  }

  fun ofDefault(): Type {
    return TypeToken.getParameterized(BaseResponse::class.java).type
  }

  fun <R> ofDefaultData(clazz: Class<R>): Type {
    return TypeToken.getParameterized(DataResponse::class.java, clazz).type
  }

  fun <R> ofDefaultDataList(clazz: Class<R>): Type {
    return TypeToken.getParameterized(DataListResponse::class.java, clazz).type
  }

}