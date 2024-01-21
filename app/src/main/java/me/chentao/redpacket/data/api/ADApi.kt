package me.chentao.redpacket.data.api

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * create by chentao on 2024-01-21.
 */
interface ADApi {

  @GET("openApi/advertising/pageList")
  fun getADList(
    @Query("current") current: Int,
    @Query("size") size: Int,
    @Query("status") status: Int
  ): Observable<ResponseBody>

  @PUT("openApi/updateViewCount/{id}")
  fun updateViewCount(
    @Path("id") id: String
  ): Observable<ResponseBody>

}