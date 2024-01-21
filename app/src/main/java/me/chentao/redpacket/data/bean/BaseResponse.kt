package me.chentao.redpacket.data.bean

import com.google.gson.annotations.SerializedName

/**
 * create by chentao on 2024-01-21.
 */
open class BaseResponse {

  @SerializedName("code")
  var code: Int = 0

  @SerializedName("msg")
  var msg: String? = null
}


class DataResponse<T : Any> : BaseResponse() {
  @SerializedName("data")
  var data: T? = null
}

class DataListResponse<T : Any> : BaseResponse() {

  @SerializedName("data")
  var data: List<T>? = null

}