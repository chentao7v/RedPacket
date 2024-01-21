package me.chentao.redpacket.data.api

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * create by chentao on 2024-01-21.
 */
interface ADApi {

  @POST("advertising/pageList")
  @FormUrlEncoded
  fun getADList(
    @Field("current") current: Int,
    @Field("size") size: Int,
    @Field("status") status: Int
  ):Observable<ResponseBody>

}