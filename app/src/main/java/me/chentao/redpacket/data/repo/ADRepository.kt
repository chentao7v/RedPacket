package me.chentao.redpacket.data.repo

import io.reactivex.rxjava3.core.Observable
import me.chentao.redpacket.data.ApiClient
import me.chentao.redpacket.data.api.ADApi
import me.chentao.redpacket.data.bean.ADItem
import me.chentao.redpacket.data.bean.BaseResponse
import me.chentao.redpacket.data.bean.DataListResponse
import me.chentao.redpacket.data.convert.DefaultApiFunction
import me.chentao.redpacket.data.convert.NetworkTransform
import me.chentao.redpacket.rxjava.ioToUiThread

/**
 * create by chentao on 2024-01-21.
 */
class ADRepository {

  private val api by lazy {
    ApiClient.createDefaultService(ADApi::class.java)
  }

  private var currentPager = 1

  fun getADList(isRefresh: Boolean): Observable<DataListResponse<ADItem>> {
    if (isRefresh) {
      currentPager = 1
    }

    return api.getADList(currentPager, 10, ADItem.STATUS_OK)
      .compose(NetworkTransform.ofDataList(ADItem::class.java))
      .flatMap(DefaultApiFunction())
      .ioToUiThread()
      .doOnNext {
        if (!isRefresh) {
          currentPager++
        }
      }
  }

  fun updateADViewCount(id: String): Observable<BaseResponse> {
    return api.updateViewCount(id)
      .compose(NetworkTransform.ofMini())
      .flatMap(DefaultApiFunction())
      .ioToUiThread()
  }

}