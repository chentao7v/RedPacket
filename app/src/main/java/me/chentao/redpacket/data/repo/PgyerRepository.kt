package me.chentao.redpacket.data.repo

import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.data.ApiClient
import me.chentao.redpacket.data.api.PgyerApi
import me.chentao.redpacket.data.bean.PgyerResponse
import me.chentao.redpacket.data.bean.PgyerUpdateInfo
import me.chentao.redpacket.data.convert.NetworkTransform
import me.chentao.redpacket.data.convert.Types

/**
 * create by chentao on 2024-01-15.
 */
class PgyerRepository {

  private val api by lazy { ApiClient.createPgyerService(PgyerApi::class.java) }

  fun checkUpdate(): Observable<PgyerResponse<PgyerUpdateInfo>> {
    return api.checkUpdate()
      .compose(NetworkTransform(Types.ofPgyer(PgyerUpdateInfo::class.java)))
  }

}