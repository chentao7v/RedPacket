package me.chentao.redpacket.data.api

import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.data.bean.User
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * create by chentao on 2024-01-21.
 */
interface UserApi {

  /**
   * 新增用户
   */
  @POST("openApi/saveAppUser")
  fun saveAppUser(
    @Body user: User
  ): Observable<ResponseBody>


  /**
   * 更新 DAY
   */
  @POST("openApi/saveDua/{userId}")
  fun saveDAU(
    @Path("userId") userId: String
  ): Observable<ResponseBody>


}