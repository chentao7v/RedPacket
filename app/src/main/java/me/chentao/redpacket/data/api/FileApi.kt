package me.chentao.redpacket.data.api

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * create by chentao on 2023-11-22.
 */
interface FileApi {

  @GET
  @Streaming
  fun download(@Url url: String): Observable<ResponseBody>

}