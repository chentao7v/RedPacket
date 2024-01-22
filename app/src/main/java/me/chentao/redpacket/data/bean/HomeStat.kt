package me.chentao.redpacket.data.bean

import com.google.gson.annotations.SerializedName

/**
 * create by chentao on 2024-01-22.
 */
class HomeStat {

  /**
   * 用户总量
   */
  @SerializedName("userCount")
  var userCount: String = ""

  /**
   * 日活
   */
  @SerializedName("dauCount")
  var dauCount: String = ""

}