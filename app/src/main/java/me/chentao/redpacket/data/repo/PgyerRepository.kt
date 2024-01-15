package me.chentao.redpacket.data.repo

import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.data.ApiClient
import me.chentao.redpacket.data.api.PgyerApi
import me.chentao.redpacket.data.bean.PgyerUpdateInfo

/**
 * create by chentao on 2024-01-15.
 */
class PgyerRepository {

  private val api by lazy { ApiClient.createPgyerService(PgyerApi::class.java) }

  fun checkUpdate(): Observable<PgyerUpdateInfo> {
    return api.checkUpdate()
  }

}