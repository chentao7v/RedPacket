package me.chentao.redpacket.data.bean

import com.google.gson.annotations.SerializedName

/**
 * create by chentao on 2024-01-18.
 */
class ADItem {

  companion object Status {

    /** 待上架 */
    const val STATUS_WAIT = 0

    /** 已上架 */
    const val STATUS_OK = 1

    /** 已下架 */
    const val STATUS_OUT_DATE = 2

    /// 跳转类型

    /**  APP 内部  */
    const val NAV_APP_INTERNAL = 0

    /** 第三方网页 */
    const val NAV_THIRD_APP = 1

    /** 外部网页 */
    const val NAV_WEB = 2

  }

  var position: Int = 0

  var image = "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF"

  var height = 0

  /// 其他参数

  /**
   * 0-待上架，1-已上架，2-已下架
   */
  @SerializedName("status")
  var status: Int = STATUS_WAIT

  /**
   * 广告标题
   */
  @SerializedName("title")
  var title: String? = null

  /**
   * 访问量
   */
  @SerializedName("viewCount")
  var viewCount: Long = 0

  /**
   * 跳转类型：0-APP内部详情,1-第三方APP，3-外部网页
   */
  @SerializedName("navType")
  var navType: String? = null

  /**
   * 跳转地址
   */
  @SerializedName("navUrl")
  var navUrl: String? = null

  /**
   * 广告图片地址
   */
  @SerializedName("sourceUrl")
  var sourceUrl: String? = null

  @SerializedName("id")
  var id: String = ""

}