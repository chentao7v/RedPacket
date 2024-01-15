package me.chentao.redpacket.data.bean

import com.google.gson.annotations.SerializedName

/**
 * create by chentao on 2024-01-15.
 */
class BasePgyer<T> {

  @SerializedName("code")
  var code: Int = 0

  @SerializedName("message")
  var message: String? = null

  @SerializedName("data")
  var data: T? = null

}