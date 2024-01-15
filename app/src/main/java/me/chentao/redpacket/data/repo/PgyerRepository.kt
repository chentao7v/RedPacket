package me.chentao.redpacket.data.repo

import me.chentao.redpacket.data.ApiClient
import me.chentao.redpacket.data.api.PgyerApi
import me.chentao.redpacket.data.bean.PgyerUpdateInfo
import retrofit2.Call

/**
 * create by chentao on 2024-01-15.
 */
class PgyerRepository {

  private val api by lazy { ApiClient.createPgyerService(PgyerApi::class.java) }

  fun checkUpdate(): Call<PgyerUpdateInfo> {
    return api.checkUpdate()
  }

}