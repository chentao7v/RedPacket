package me.chentao.redpacket.data.bean

import com.google.gson.annotations.SerializedName

/**
 * create by chentao on 2024-01-15.
 */
class PgyerUpdateInfo {

  /**
   * 蒲公英生成的用于区分历史版本的build号
   */
  @SerializedName("buildBuildVersion")
  var buildBuildVersion: Int = 0

  /**
   * 强制更新版本号（未设置强置更新默认为空）
   */
  @SerializedName("forceUpdateVersion")
  var forceUpdateVersion: String? = null

  /**
   * 强制更新的版本编号
   */
  @SerializedName("forceUpdateVersionNo")
  var forceUpdateVersionNo: String? = null

  /**
   * 是否强制更新
   */
  @SerializedName("needForceUpdate")
  var needForceUpdate: Boolean = false

  /**
   * 应用安装地址
   */
  @SerializedName("downloadURL")
  var downloadURL: String? = null

  /**
   * 是否有新版本
   */
  @SerializedName("buildHaveNewVersion")
  var buildHaveNewVersion: Boolean = false

  /**
   * 上传包的版本编号，默认为1 (即编译的版本号，一般来说，编译一次会变动一次这个版本号, 在 Android 上叫 Version Code。
   * 对于 iOS 来说，是字符串类型；对于 Android 来说是一个整数。例如：1001，28等。)
   */
  @SerializedName("buildVersionNo")
  var buildVersionNo: String? = null

  /**
   * 版本号, 默认为1.0 (是应用向用户宣传时候用到的标识，例如：1.1、8.2.1等。)
   */
  @SerializedName("buildVersion")
  var buildVersion: String? = null

  /**
   * 应用短链接
   */
  @SerializedName("buildShortcutUrl")
  var buildShortcutUrl: String? = null

  /**
   * 应用更新说明
   */
  @SerializedName("buildUpdateDescription")
  var buildUpdateDescription: String? = null


}