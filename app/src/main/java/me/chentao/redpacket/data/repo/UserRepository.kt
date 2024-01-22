package me.chentao.redpacket.data.repo

import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.data.ApiClient
import me.chentao.redpacket.data.api.UserApi
import me.chentao.redpacket.data.bean.BaseResponse
import me.chentao.redpacket.data.bean.DataResponse
import me.chentao.redpacket.data.bean.HomeStat
import me.chentao.redpacket.data.bean.User
import me.chentao.redpacket.data.convert.DefaultApiFunction
import me.chentao.redpacket.data.convert.NetworkTransform
import me.chentao.redpacket.rxjava.ioToUiThread

/**
 * create by chentao on 2024-01-21.
 */
class UserRepository {

  private val api by lazy {
    ApiClient.createDefaultService(UserApi::class.java)
  }

  fun saveAppUser(id: String): Observable<BaseResponse> {
    val user = User()
    user.id = id
    return api.saveAppUser(user)
      .compose(NetworkTransform.ofMini())
      .flatMap(DefaultApiFunction())
      .ioToUiThread()
  }

  fun saveDAU(id: String): Observable<BaseResponse> {
    return api.saveDAU(id)
      .compose(NetworkTransform.ofMini())
      .flatMap(DefaultApiFunction())
      .ioToUiThread()
  }

  fun getHomeStat(): Observable<DataResponse<HomeStat>> {
    return api.getAppHomeStat()
      .compose(NetworkTransform.ofData(HomeStat::class.java))
      .flatMap(DefaultApiFunction())
      .ioToUiThread()
  }

}