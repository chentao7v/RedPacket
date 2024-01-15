package me.chentao.redpacket.data.api

import me.chentao.redpacket.data.bean.PgyerUpdateInfo
import me.chentao.redpacket.utils.appVersionName
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * create by chentao on 2024-01-15.
 */
interface PgyerApi {

  companion object {
    private const val API_KEY = "8be749b3704d9d086972b13c5c2a05c3"
    private const val APP_KEY = "d3aaa2220e4a71cd78b0683742559c44"
  }

  @POST("app/check")
  @FormUrlEncoded
  fun checkUpdate(
    @Field("_api_key") apiKey: String = API_KEY,
    @Field("appKey") appKey: String = APP_KEY,
    @Field("buildVersion") versionName: String = appVersionName
  ): Call<PgyerUpdateInfo>


}